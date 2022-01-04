package sw.laux.Studentrack.application.exceptions;

public class StudentrackPasswordWrongException extends StudentrackObjectException{
    public StudentrackPasswordWrongException(Class<?> studentrackClass, Object object) {
        super(studentrackClass, object, "wrong password");
    }
}
