package sw.laux.Studentrack.presentation.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sw.laux.Studentrack.application.exceptions.StudentrackAuthenticationException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.exceptions.StudentrackOperationNotAllowedException;

@RestControllerAdvice
public class RestControllerExceptionHandler {
    @ExceptionHandler(value={StudentrackObjectNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String studentrackObjectNotFoundException(StudentrackObjectNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value={StudentrackAuthenticationException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String studentrackAuthenticationException(StudentrackAuthenticationException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value={StudentrackObjectAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public String studentrackObjectAlreadyExistsException(StudentrackObjectAlreadyExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value={StudentrackOperationNotAllowedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String studentrackOperationNotAllowedException(StudentrackOperationNotAllowedException exception) {
        return exception.getMessage();
    }
}
