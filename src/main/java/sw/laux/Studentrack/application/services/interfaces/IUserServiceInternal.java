package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.User;

public interface IUserServiceInternal extends IUserService{
    User registerUser(User user) throws StudentrackObjectAlreadyExistsException;
    User updateUser(User user);
    Student findStudent(Student student) throws StudentrackObjectNotFoundException;
    Student findStudent(Long userId) throws StudentrackObjectNotFoundException;
}
