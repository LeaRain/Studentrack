package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.exceptions.StudentrackOperationNotAllowedException;
import sw.laux.Studentrack.application.exceptions.StudentrackPasswordWrongException;
import sw.laux.Studentrack.persistence.entities.Faculty;
import sw.laux.Studentrack.persistence.entities.Lecturer;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.User;

import javax.transaction.Transactional;
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
    User changeUserPassword(User user, String oldPassword, String newPassword) throws StudentrackObjectNotFoundException, StudentrackPasswordWrongException;
    boolean validatePasswordForUser(User user, String password) throws StudentrackObjectNotFoundException;
    void deleteUser(User user) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException;
    @Transactional
    void deleteStudent(Student student) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException;
    @Transactional
    void deleteLecturer(Lecturer lecturer) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException;
    Faculty findFaculty(Faculty faculty) throws StudentrackObjectNotFoundException;
    Student calculateCurrentECTSOfStudent(Student student) throws StudentrackObjectNotFoundException;
}
