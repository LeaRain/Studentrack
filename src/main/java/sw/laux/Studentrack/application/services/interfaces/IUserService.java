package sw.laux.Studentrack.application.services.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;
import sw.laux.Studentrack.persistence.entities.Grade;
import sw.laux.Studentrack.persistence.entities.TimeInvest;
import sw.laux.Studentrack.persistence.entities.User;
import sw.laux.Studentrack.persistence.entities.Module;

import java.util.Map;

public interface IUserService extends UserDetailsService {
    User loginUser(User user);
    User getUserData(User user);
    Grade getGrade(User user, Module module);
    Map<Module, Grade> getAllGrades(User user);
    TimeInvest getTimeInvest(User user, Module module);

}
