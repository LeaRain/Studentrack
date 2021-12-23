package sw.laux.Studentrack.application.exceptions;

public class StudentrackObjectException extends Throwable{
    public StudentrackObjectException(Class<?> studentrackClass, Object object, String detailMessage) {
        super("Class " + studentrackClass + " as/for " + object + " " + detailMessage);
    }
}
