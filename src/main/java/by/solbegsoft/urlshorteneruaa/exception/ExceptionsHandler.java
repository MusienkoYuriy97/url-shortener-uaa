package by.solbegsoft.urlshorteneruaa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserDataException.class)
    protected ResponseEntity<?> userData(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<?> auth(Exception e) {
        return new ResponseEntity<>("Wrong email/password", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ActiveKeyNotValidException.class)
    protected ResponseEntity<?> activate(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoActivatedAccountException.class)
    protected ResponseEntity<?> noActivated(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}