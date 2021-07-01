package by.solbegsoft.urlshorteneruaa.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoActivatedAccountException  extends Exception{
    private HttpStatus httpStatus;

    public NoActivatedAccountException() {
    }
    public NoActivatedAccountException(String message) {
        super(message);
    }

    public NoActivatedAccountException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
