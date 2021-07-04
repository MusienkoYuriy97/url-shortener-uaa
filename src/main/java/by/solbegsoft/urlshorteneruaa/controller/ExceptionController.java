package by.solbegsoft.urlshorteneruaa.controller;


import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserDataException.class)
    protected ResponseEntity<?> userDataException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> runtime(){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
