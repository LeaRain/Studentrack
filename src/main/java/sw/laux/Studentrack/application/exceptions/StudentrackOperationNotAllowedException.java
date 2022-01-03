package sw.laux.Studentrack.application.exceptions;

public class StudentrackOperationNotAllowedException extends StudentrackObjectException{
    public StudentrackOperationNotAllowedException(Class<?> studentrackClass, Object object) {
        super(studentrackClass, object, " not allowed!");
    }
}
