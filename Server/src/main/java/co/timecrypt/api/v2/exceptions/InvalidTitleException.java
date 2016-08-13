package co.timecrypt.api.v2.exceptions;

/**
 * Throw this when title length is out of bounds or it contains invalid characters.
 */
public class InvalidTitleException extends Exception {

    public InvalidTitleException(String message) {
        super(message);
    }

    public InvalidTitleException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTitleException(Throwable cause) {
        super(cause);
    }

    public InvalidTitleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
