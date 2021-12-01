package sw.laux.Studentrack.application.services;

import sw.laux.Studentrack.persistence.entities.Grade;
import sw.laux.Studentrack.persistence.entities.User;

import java.util.Map;

public interface IUserService {
    User loginUser(User user);
    User getUserData(User user);
    Grade getGrade(User user, Module module);
    Map<Module, Grade> getAllGrades(User user);
    TimeInvest getTimeInvest(User user, Module module);

}
