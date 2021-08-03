package by.solbegsoft.urlshorteneruaa.exception;

import lombok.Setter;

import java.time.LocalDateTime;

@Setter
public class CustomErrorResponse {
    private String error;
    private int status;
    private LocalDateTime timestamp;
}
