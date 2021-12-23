package sw.laux.Studentrack.application.exceptions;

import sw.laux.Studentrack.persistence.entities.Module;

public class StudentrackObjectAlreadyExistsException extends StudentrackObjectException{
    public StudentrackObjectAlreadyExistsException(Class<?> studentrackClass, Object object) {
        super(studentrackClass, object, " already exists!");

    }

}
