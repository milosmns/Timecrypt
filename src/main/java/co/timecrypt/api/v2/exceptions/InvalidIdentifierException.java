
package co.timecrypt.api.v2.exceptions;

/**
 * Throw this when ID is not available, not valid, or non-existent in the data store.
 */
public class InvalidIdentifierException extends Exception {

    public InvalidIdentifierException() {
    }

    public InvalidIdentifierException(String message) {
        super(message);
    }

    public InvalidIdentifierException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidIdentifierException(Throwable cause) {
        super(cause);
    }

    public InvalidIdentifierException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
