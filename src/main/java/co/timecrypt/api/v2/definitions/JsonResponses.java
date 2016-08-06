package co.timecrypt.api.v2.definitions;

/**
 * Holder class containing all possible JSON responses from the API.
 */
public final class JsonResponses {

    /**
     * A superclass for all Json responses with {@code code} and {@code status}.
     */
    public static class TimecryptResponse {
        public int status_code;

        public TimecryptResponse(int code) {
            status_code = code;
        }
    }

    /**
     * A generic error containing the error code.
     */
    public static final class Error extends TimecryptResponse {
        public Error(int code) {
            super(code);
        }
    }

    /**
     * A normal response from {@link co.timecrypt.api.v2.servlets.LockCheckServlet} API call.
     */
    public static final class LockCheckResponse extends TimecryptResponse {
        public final boolean locked;

        public LockCheckResponse(boolean locked) {
            super(ErrorCode.NONE);
            this.locked = locked;
        }
    }

}
