package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.*;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.repository.FacultyRepository;
import sw.laux.Studentrack.persistence.repository.UserRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceInternal implements IUserServiceInternal {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private FacultyRepository facultyRepo;

    @Autowired
    private IModuleService moduleService;


    @Override
    public User loginUser(User user) {
        return null;
    }

    @Override
    public User getUserData(User user) {
        return null;
    }

    @Override
    public Grade getGrade(User user, Module module) {
        return null;
    }

    @Override
    public Map<Module, Grade> getAllGrades(User user) {
        return null;
    }

    @Override
    public TimeInvest getTimeInvest(User user, Module module) {
        return null;
    }

    @Override
    public User registerUser(User user) throws StudentrackObjectAlreadyExistsException {
        var mailAddressUser = userRepo.findByMailAddress(user.getMailAddress());

        if (mailAddressUser.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepo.save(user);
        }

        else {
            throw new StudentrackObjectAlreadyExistsException(user.getClass(), user);
        }
    }

    @Override
    public User updateUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public Student findStudent(Student student) throws StudentrackObjectNotFoundException {
        return findStudent(student.getUserId());
    }

    @Override
    public Student findStudent(Long userId) throws StudentrackObjectNotFoundException {
        var userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(Student.class, userId);
        }

        return (Student) userOptional.get();

    }

    @Override
    public Collection<Faculty> getAllFaculties() {
        return (Collection<Faculty>) facultyRepo.findAll();
    }

    @Override
    public User updateUserWithNamesAndMailAddress(User user) throws StudentrackObjectNotFoundException, StudentrackObjectAlreadyExistsException {
        var userOptional = userRepo.findById(user.getUserId());

        if (userOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(user.getClass(), user);
        }

        var foundUser = userOptional.get();

        foundUser.setFirstName(user.getFirstName());
        foundUser.setLastName(user.getLastName());

        // Handle mail address change
        if (!Objects.equals(foundUser.getMailAddress(), user.getMailAddress())) {
            try {
                // Find out if user with mail address already exists
                findUserByMailAddress(user);
                throw new StudentrackObjectAlreadyExistsException(user.getClass(), user.getMailAddress());
            }

            catch (StudentrackObjectNotFoundException ignored) {}
        }

        foundUser.setMailAddress(user.getMailAddress());
        userRepo.save(foundUser);

        return foundUser;
    }

    @Override
    public User findUserByMailAddress(User user) throws StudentrackObjectNotFoundException {
        return findUserByMailAddress(user.getMailAddress());
    }

    @Override
    public User findUserByMailAddress(String mailAddress) throws StudentrackObjectNotFoundException {
        var userOptional = userRepo.findByMailAddress(mailAddress);

        if (userOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(User.class, mailAddress);
        }

        return userOptional.get();
    }

    @Override
    public StudentDTO findStudentDTOByMailAddress(String mailAddress) throws StudentrackObjectNotFoundException {
        var user = findUserByMailAddress(mailAddress);

        if (!(user instanceof Student student)) {
            throw new StudentrackObjectNotFoundException(StudentDTO.class, mailAddress);
        }

        return buildStudentDTOBasedOnStudent(student);

    }

    @Override
    public StudentDTO findStudentDTOByUserId(Long userId) throws StudentrackObjectNotFoundException {
        var student = findStudent(userId);
        return buildStudentDTOBasedOnStudent(student);
    }

    @Override
    public StudentDTO buildStudentDTOBasedOnStudent(Student student) {
        var studentDto = new StudentDTO();
        studentDto.setUserId(student.getUserId());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setMailAddress(student.getMailAddress());
        studentDto.setEcts(student.getEcts());

        return studentDto;
    }

    @Override
    public User changeUserPassword(User user, String oldPassword, String newPassword) throws StudentrackObjectNotFoundException, StudentrackPasswordWrongException {
        if (!validatePasswordForUser(user, oldPassword)) {
            throw new StudentrackPasswordWrongException(user.getClass(), user);
        }

        user = findUserByMailAddress(user);
        user.setPassword(passwordEncoder.encode(newPassword));

        return userRepo.save(user);
    }

    @Override
    public boolean validatePasswordForUser(User user, String password) throws StudentrackObjectNotFoundException{
        user = findUserByMailAddress(user);
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void deleteUser(User user) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException {
        // Existence check -> exception if user is not found
        findUserByMailAddress(user);

        // Prepare deletion, so clean up before deleting lecturer
        if (user instanceof Lecturer) {
            deleteLecturer((Lecturer) user);
        }

        userRepo.delete(user);

        // Clean up after deleting student
        if (user instanceof Student) {
            deleteStudent((Student) user);
        }
    }

    @Override
    public void deleteStudent(Student student) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException {
        // Edge case: Student has ModuleResults for Module without Lecturer, because Lecturer has deleted their account before
        moduleService.deleteModulesWithoutLecturerAndModuleResults();
    }

    @Override
    public void deleteLecturer(Lecturer lecturer) throws StudentrackObjectNotFoundException {
        // Is the lecturer currently the dean of a faculty?
        var faculty = lecturer.getDeanOfFaculty();
        // Is dean -> remove references
        if (faculty != null) {
            faculty = findFaculty(faculty);
            faculty.setDean(null);
            lecturer.setDeanOfFaculty(null);
            facultyRepo.save(faculty);
        }

        moduleService.deleteAllModulesOfLecturer(lecturer);

    }

    @Override
    public Faculty findFaculty(Faculty faculty) throws StudentrackObjectNotFoundException {
        var facultyOptional = facultyRepo.findById(faculty.getFacultyId());

        if (facultyOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(faculty.getClass(), faculty);
        }

        return facultyOptional.get();
    }

    @Override
    public Student calculateCurrentECTSOfStudent(Student student) throws StudentrackObjectNotFoundException {
        student = findStudent(student);
        var ects = moduleService.calculateECTSOfStudent(student);
        student.setEcts(ects);
        return userRepo.save(student);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        var user = userRepo.findByMailAddress(s);

        if (user.isEmpty()) {
            // Exception defined by Spring Security, not Studentrack
            throw new UsernameNotFoundException("User with mail address " + s + " not found!");
        }

        return user.get();
    }
}
