package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.UserAlreadyRegisteredException;
import sw.laux.Studentrack.persistence.entities.User;

public interface IUserServiceInternal extends IUserService{
    User registerUser(User user) throws UserAlreadyRegisteredException;
    User updateUser(User user);
}
