package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
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

        if (mailAddressUser == null) {
            return userRepo.save(user);
        }

        else {
            throw new UserAlreadyRegisteredException("User with mail address " + user.getMailAddress() + " already exists!");
        }
    }
}
