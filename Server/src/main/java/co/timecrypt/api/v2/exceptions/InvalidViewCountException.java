package co.timecrypt.api.v2.exceptions;

/**
 * Throw this view count parameter is out of bounds or not in valid number format.
 */
public class InvalidViewCountException extends Exception {

    public InvalidViewCountException(String message) {
        super(message);
    }

    public InvalidViewCountException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidViewCountException(Throwable cause) {
        super(cause);
    }

    public InvalidViewCountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
