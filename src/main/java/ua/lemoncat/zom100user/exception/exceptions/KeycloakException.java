package ua.lemoncat.zom100user.exception.exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class KeycloakException extends RuntimeException{

    private final HttpStatus status;

    public KeycloakException(String message, HttpStatus httpStatus) {
        super(message.substring(17, message.length() - 2));
        status = httpStatus;
    }

}
