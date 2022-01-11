package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.exceptions.StudentrackOperationNotAllowedException;
import sw.laux.Studentrack.application.exceptions.StudentrackAuthenticationException;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.application.DTO.APIKeyDTO;
import sw.laux.Studentrack.application.DTO.StudentDTO;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;

public interface IUserServiceInternal extends IUserService{
    User registerUser(User user) throws StudentrackObjectAlreadyExistsException;
    User updateUser(User user);
    Student findStudent(Student student) throws StudentrackObjectNotFoundException;
    Student findStudent(Long userId) throws StudentrackObjectNotFoundException;
    Collection<Faculty> getAllFaculties();
    User updateUserWithNamesAndMailAddress(User user) throws StudentrackObjectNotFoundException, StudentrackObjectAlreadyExistsException;
    User findUserByMailAddress(User user) throws StudentrackObjectNotFoundException;
    User findUserByMailAddress(String mailAddress) throws StudentrackObjectNotFoundException;
    StudentDTO findStudentDTOByMailAddress(String mailAddress) throws StudentrackObjectNotFoundException;
    StudentDTO findStudentDTOByUserId(Long userId) throws StudentrackObjectNotFoundException;
    StudentDTO buildStudentDTOBasedOnStudent(Student student);
    User changeUserPassword(User user, String oldPassword, String newPassword) throws StudentrackObjectNotFoundException, StudentrackAuthenticationException;
    boolean validatePasswordForUser(User user, String password) throws StudentrackObjectNotFoundException;
    void deleteUser(User user) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException;
    @Transactional
    void deleteStudent(Student student) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException;
    @Transactional
    void deleteLecturer(Lecturer lecturer) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException;
    Faculty findFaculty(Faculty faculty) throws StudentrackObjectNotFoundException;
    Student calculateCurrentECTSOfStudent(Student student) throws StudentrackObjectNotFoundException;
    Developer preregisterDeveloper(Developer developer) throws StudentrackObjectAlreadyExistsException;
    Developer commitRegisterDeveloper(Developer developer);
    APIKey generateAPIKeyForDeveloper(Developer developer);
    void validateAPIKey(APIKeyDTO apiKeyDTO) throws StudentrackObjectNotFoundException, StudentrackAuthenticationException;
    void validateKeyExpirationDate(Developer developer, APIKeyDTO apiKeyDTO) throws StudentrackAuthenticationException;
    boolean existsValidKeyForDeveloper(Developer developer);
    boolean isDateExpired(Date date);
    String generateKey();
    Date getDateInOneMonth();
}
