package by.solbegsoft.urlshorteneruaa.client.exception;

public class UrlDataException extends RuntimeException{
    public UrlDataException() {
    }

    public UrlDataException(String message) {
        super(message);
    }

    public UrlDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
