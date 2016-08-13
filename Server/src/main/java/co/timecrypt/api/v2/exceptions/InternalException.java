package co.timecrypt.api.v2.exceptions;

/**
 * Throw this when something internally goes wrong, but you don't want to crash the server.
 */
public class InternalException extends Exception {

    public InternalException(String message) {
        super(message);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalException(Throwable cause) {
        super(cause);
    }

    public InternalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
