package sw.laux.Studentrack.application.exceptions;

public class StudentrackObjectNotFoundException extends StudentrackObjectException{
    public StudentrackObjectNotFoundException(Class<?> studentrackClass, Object object) {
        super(studentrackClass, object, " not found!");
    }
}
