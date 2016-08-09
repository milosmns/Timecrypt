package co.timecrypt.api.v2.exceptions;

/**
 * Throw this when destruct date is out of bounds or not in valid number format.
 */
public class InvalidDestructDateException extends Exception {

    public InvalidDestructDateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidDestructDateException(String message) {
        super(message);
    }

    public InvalidDestructDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDestructDateException(Throwable cause) {
        super(cause);
    }

}
