package sw.laux.Studentrack.presentation.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;

@RestControllerAdvice
public class RestControllerExceptionHandler {
    @ExceptionHandler(value={StudentrackObjectNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String studentrackObjectNotFoundException(StudentrackObjectNotFoundException exception) {
        return exception.getMessage();
    }
}
