package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.UserAlreadyRegisteredException;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.Grade;
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
    public User registerUser(User user) throws UserAlreadyRegisteredException {
        var mailAddressUser = userRepo.findByMailAddress(user.getMailAddress());

        if (mailAddressUser.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepo.save(user);
        }

        else {
            throw new UserAlreadyRegisteredException("User with mail address " + user.getMailAddress() + " already exists!");
        }
    }

    @Override
    public User updateUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        var user = userRepo.findByMailAddress(s);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with mail address " + s + " not found!");
        }

        return user.get();
    }
}
