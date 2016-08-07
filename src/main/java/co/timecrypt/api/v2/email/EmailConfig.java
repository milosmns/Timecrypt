package co.timecrypt.api.v2.email;

/**
 * Basic email API configuration for the MailGun implementation.
 */
public class EmailConfig {

    public static final String PROP_MAIL = "mailgun_api_key";
    public static final String PROP_API = "api";
    public static final String PROP_FROM = "from";
    public static final String PROP_TO = "to";
    public static final String PROP_SUBJECT = "subject";
    public static final String PROP_TEXT = "text";

    public static final String ENV_HOST = "MAILGUN_API_KEY";

    public static final String EMAIL_DOMAIN = "timecrypt.co";
    public static final String EMAIL_SUBJECT = "Someone sent you a secret message";
    public static final String EMAIL_FROM_NAME = "Timecrypt";
    public static final String EMAIL_FROM_ADDR = "messages@" + EMAIL_DOMAIN;

    public static final String API_URL = "https://api.mailgun.net/v3/" + EMAIL_DOMAIN + "/messages";

}
