package co.timecrypt.api.v2.exceptions;

/**
 * Throw this when password length is out of bounds or it contains invalid characters.
 */
public class InvalidPasswordException extends Exception {

    public InvalidPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPasswordException(Throwable cause) {
        super(cause);
    }

}
