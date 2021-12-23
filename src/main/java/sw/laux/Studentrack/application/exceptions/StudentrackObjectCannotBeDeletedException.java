package sw.laux.Studentrack.application.exceptions;

public class StudentrackObjectCannotBeDeletedException extends StudentrackObjectException{
    public StudentrackObjectCannotBeDeletedException(Class<?> studentrackClass, Object object) {
        super(studentrackClass, object, " cannot be deleted!");
    }
}
