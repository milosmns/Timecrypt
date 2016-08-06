package co.timecrypt.api.v2.definitions;

/**
 * Holder class containing all possible JSON responses from the API.
 */
public final class JsonResponses {

    /**
     * A generic error containing the error code.
     */
    static final class Error {
        public final int code;
        public final boolean success;

        public Error(int code) {
            success = false;
            this.code = code;
        }
    }

}
