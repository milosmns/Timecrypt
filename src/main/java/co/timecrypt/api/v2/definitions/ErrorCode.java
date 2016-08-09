package co.timecrypt.api.v2.definitions;

/**
 * Holder class containing all user-facing error codes from the API.
 */
public final class ErrorCode {

    /**
     * No error.
     */
    public static final int NONE = 0;

    /**
     * Internal error, check the log.
     */
    public static final int INTERNAL = -1;

    /**
     * Message ID was not supplied.
     */
    public static final int MISSING_ID = -2;

    /**
     * Message ID is not valid.
     */
    public static final int INVALID_ID = -3;

    /**
     * Message text is not provided.
     */
    public static final int MISSING_TEXT = -4;

    /**
     * Title length is out of bounds.
     */
    public static final int INVALID_TITLE = -5;

    /**
     * Text length is out of bounds.
     */
    public static final int INVALID_TEXT = -6;

    /**
     * View count is out of bounds.
     */
    public static final int INVALID_VIEW_COUNT = -7;

    /**
     * No password found and no default password configured.
     */
    public static final int INVALID_PASSWORD = -8;

    /**
     * Self-destruct date is not in valid format or it is out of bounds.
     */
    public static final int INVALID_DESTRUCT_DATE = -9;

    /**
     * Invitation email is not in valid format.
     */
    public static final int INVALID_EMAIL_TO = -10;

    /**
     * Notification email is not in valid format.
     */
    public static final int INVALID_EMAIL_FROM = -11;

}
