package co.timecrypt.api.v2.exceptions;

/**
 * Throw this when email is not in valid format.
 */
public class InvalidEmailException extends Exception {

    public enum Type {
        TO, FROM
    }

    private Type type;

    public InvalidEmailException(Type type, String message) {
        super(message);
        this.type = type;
    }

    public InvalidEmailException(Type type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
    }

    public InvalidEmailException(Type type, Throwable cause) {
        super(cause);
        this.type = type;
    }

    public InvalidEmailException(Type type, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

}
