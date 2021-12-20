package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.UserAlreadyRegisteredException;
import sw.laux.Studentrack.application.exceptions.UserNotFoundException;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.User;

public interface IUserServiceInternal extends IUserService{
    User registerUser(User user) throws UserAlreadyRegisteredException;
    User updateUser(User user);
    Student findStudent(Student student) throws UserNotFoundException;
    Student findStudent(Long userId) throws UserNotFoundException;
}
