package ua.lemoncat.zom100user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ua.lemoncat.zom100user.DTO.ErrorMessage;
import ua.lemoncat.zom100user.exception.exceptions.KeycloakException;
import ua.lemoncat.zom100user.exception.exceptions.UserNotExistException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler({KeycloakException.class})
    public final ResponseEntity<ErrorMessage> handleKeycloakException(KeycloakException ex) {
        log.info(ex.getStatus() + " " + ex.getMessage());

        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), ex.getStatus());
    }

    @ExceptionHandler({UserNotExistException.class})
    public final ResponseEntity<ErrorMessage> handleUserNotExistException(UserNotExistException ex) {
        log.info(HttpStatus.NOT_FOUND + " " + ex.getMessage());

        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Map<String, String> handleValidationExceptions(HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllValidationResults().forEach((error) -> {
            String fieldName = error.getMethodParameter().getParameterName();
            String errorMessage = error.getResolvableErrors().get(0).getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
