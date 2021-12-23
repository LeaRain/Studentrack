package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.Grade;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.TimeInvest;
import sw.laux.Studentrack.persistence.entities.User;
import sw.laux.Studentrack.persistence.repository.UserRepository;

import java.util.Map;

@Service
public class UserServiceInternal implements IUserServiceInternal {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


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
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        var user = userRepo.findByMailAddress(s);

        if (user.isEmpty()) {
            // Exception defined by Spring Security, not Studentrack
            throw new UsernameNotFoundException("User with mail address " + s + " not found!");
        }

        return user.get();
    }
}
