package co.timecrypt.api.v2.definitions;

import java.util.Date;

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

    /**
     * A normal response from {@link co.timecrypt.api.v2.servlets.CreateServlet} API call.
     */
    public static final class CreateResponse extends TimecryptResponse {

        public final String id;

        public CreateResponse(String id) {
            super(ErrorCode.NONE);
            this.id = id;
        }
    }

    /**
     * A normal response from {@link co.timecrypt.api.v2.servlets.ReadServlet} API call.
     */
    public static final class ReadResponse extends TimecryptResponse {

        String text;
        String title;
        int view_count;
        Date destruct_date;

        public ReadResponse(String text, String title, int viewCount, Date destructDate) {
            super(ErrorCode.NONE);
            this.text = text;
            this.title = title;
            this.view_count = viewCount;
            this.destruct_date = destructDate;
        }
    }

}
