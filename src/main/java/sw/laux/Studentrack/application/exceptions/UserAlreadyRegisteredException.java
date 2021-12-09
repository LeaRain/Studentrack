package sw.laux.Studentrack.application.exceptions;

public class UserAlreadyRegisteredException extends Throwable{
    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
