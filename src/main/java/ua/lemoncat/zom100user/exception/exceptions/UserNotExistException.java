package ua.lemoncat.zom100user.exception.exceptions;

public class UserNotExistException extends RuntimeException{

    public UserNotExistException() {
        super("error.user-not-exist");
    }
}
