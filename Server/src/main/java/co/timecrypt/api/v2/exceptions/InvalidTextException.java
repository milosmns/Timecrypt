package co.timecrypt.api.v2.exceptions;

/**
 * Throw this when message text is empty or contains invalid characters.
 */
public class InvalidTextException extends Exception {

    public InvalidTextException(String message) {
        super(message);
    }

    public InvalidTextException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTextException(Throwable cause) {
        super(cause);
    }

    public InvalidTextException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
