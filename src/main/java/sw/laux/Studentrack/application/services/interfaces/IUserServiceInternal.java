package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.persistence.entities.User;

public interface IUserServiceInternal extends IUserService{
    User registerUser(User user);
}
