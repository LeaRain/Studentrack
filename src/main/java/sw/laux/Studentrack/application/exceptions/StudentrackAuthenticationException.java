package sw.laux.Studentrack.application.exceptions;

public class StudentrackAuthenticationException extends StudentrackObjectException{
    public StudentrackAuthenticationException(Class<?> studentrackClass, Object object) {
        super(studentrackClass, object, "Authentication failed!");
    }
}
