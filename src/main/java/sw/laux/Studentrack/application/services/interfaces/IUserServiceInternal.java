package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.persistence.entities.Faculty;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.User;

import java.util.Collection;

public interface IUserServiceInternal extends IUserService{
    User registerUser(User user) throws StudentrackObjectAlreadyExistsException;
    User updateUser(User user);
    Student findStudent(Student student) throws StudentrackObjectNotFoundException;
    Student findStudent(Long userId) throws StudentrackObjectNotFoundException;
    Collection<Faculty> getAllFaculties();
    User updateUserWithNamesAndMailAddress(User user) throws StudentrackObjectNotFoundException, StudentrackObjectAlreadyExistsException;
    User findUserByMailAddress(User user) throws StudentrackObjectNotFoundException;
    User findUserByMailAddress(String mailAddress) throws StudentrackObjectNotFoundException;
}
