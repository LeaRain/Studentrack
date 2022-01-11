package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.*;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.IUserService;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.application.DTO.APIKeyDTO;
import sw.laux.Studentrack.application.DTO.StudentDTO;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.repository.FacultyRepository;
import sw.laux.Studentrack.persistence.repository.UserRepository;

import java.util.*;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private FacultyRepository facultyRepo;

    @Autowired
    private IModuleService moduleService;

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
    public Student upgradeStudentToPremium(Student student) throws StudentrackAuthenticationException, StudentrackObjectNotFoundException {
        student = findStudent(student);

        if (student.isPremiumUser()) {
            return student;
        }

        // TODO: Call Banking Service
        var bankingSuccess = new Random().nextBoolean();

        if (!bankingSuccess) {
            throw new StudentrackAuthenticationException(student.getClass(), student);
        }

        student.setPremiumUser(true);
        return userRepo.save(student);
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
    public User changeUserPassword(User user, String oldPassword, String newPassword) throws StudentrackObjectNotFoundException, StudentrackAuthenticationException {
        if (!validatePasswordForUser(user, oldPassword)) {
            throw new StudentrackAuthenticationException(user.getClass(), user);
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
    public Developer preregisterDeveloper(Developer developer) throws StudentrackObjectAlreadyExistsException {
        try {
            var user = findUserByMailAddress(developer.getMailAddress());

            // check for developer account and currently valid key
            if (!(user instanceof Developer && !existsValidKeyForDeveloper((Developer) user))) {
                throw new StudentrackObjectAlreadyExistsException(developer.getClass(), developer);
            }

        } catch (StudentrackObjectNotFoundException ignored) {
        }
        var key = generateAPIKeyForDeveloper(developer);
        developer.setKey(key);
        return developer;
    }

    @Override
    public Developer commitRegisterDeveloper(Developer developer) {
        var apiKey = developer.getKey();
        apiKey.setKey(passwordEncoder.encode(apiKey.getKey()));
        return userRepo.save(developer);
    }

    @Override
    public APIKey generateAPIKeyForDeveloper(Developer developer) {
        var apiKey = new APIKey();
        apiKey.setDeveloper(developer);
        apiKey.setKey(generateKey());
        apiKey.setExpirationDate(getDateInOneMonth());
        return apiKey;
    }

    @Override
    public void validateAPIKey(APIKeyDTO apiKeyDTO) throws StudentrackObjectNotFoundException, StudentrackAuthenticationException {
        var user = findUserByMailAddress(apiKeyDTO.getMailAddress());

        if (!(user instanceof Developer developer)) {
            throw new StudentrackObjectNotFoundException(Developer.class, apiKeyDTO);
        }

        var developerKey = developer.getKey();
        // Exception if key is expired
        validateKeyExpirationDate(developer, apiKeyDTO);

        if (!(passwordEncoder.matches(apiKeyDTO.getKey(), developerKey.getKey()))) {
            throw new StudentrackAuthenticationException(APIKeyDTO.class, apiKeyDTO);
        }
    }

    @Override
    public void validateKeyExpirationDate(Developer developer, APIKeyDTO apiKeyDTO) throws StudentrackAuthenticationException {
        var developerKey = developer.getKey();
        var expirationDate = developerKey.getExpirationDate();

        if (isDateExpired(expirationDate)) {
            throw new StudentrackAuthenticationException(Developer.class, developer);
        }
    }

    @Override
    public boolean existsValidKeyForDeveloper(Developer developer) {
        var apiKey = developer.getKey();

        if (apiKey == null) {
            return false;
        }

        var expirationDate = apiKey.getExpirationDate();
        return !(isDateExpired(expirationDate));
    }

    @Override
    public boolean isDateExpired(Date date) {
        var now = new Date();
        // date is after now -> expired
        return now.after(date);
    }

    @Override
    public String generateKey() {
        // Uses Secure Random -> considered as secure enough for example key generation
        return UUID.randomUUID().toString();
    }

    @Override
    public Date getDateInOneMonth() {
        var calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
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
