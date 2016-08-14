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
    public static final String PROP_HTML = "html";

    public static final String ENV_HOST = "MAILGUN_API_KEY";

    public static final String EMAIL_DOMAIN = "timecrypt.co";
    public static final String EMAIL_SUBJECT = "Someone sent you a secret message";
    public static final String EMAIL_FROM_NAME = "Timecrypt";
    public static final String EMAIL_FROM_ADDR = "messages@" + EMAIL_DOMAIN;

    public static final String API_URL = "https://api.mailgun.net/v3/" + EMAIL_DOMAIN + "/messages";

    public static final String TEMPLATE_URL = "\\{\\{app_url\\}\\}";
    public static final String TEMPLATE_FACEBOOK = "\\{\\{app_facebook\\}\\}";
    public static final String TEMPLATE_TWITTER = "\\{\\{app_twitter\\}\\}";
    public static final String TEMPLATE_GOOGLE_PLUS = "\\{\\{app_google_plus\\}\\}";
    public static final String TEMPLATE_GIT_HUB = "\\{\\{app_git_hub\\}\\}";

    public static final String TEMPLATE_MESSAGE_ID = "\\{\\{message_id\\}\\}";
    public static final String TEMPLATE_SELF_DESTRUCT = "\\{\\{message_self_destruct\\}\\}";
    public static final String TEMPLATE_VIEW_COUNT = "\\{\\{message_view_count\\}\\}";

    public static final String TEMPLATE_LOGO = "\\{\\{image_logo\\}\\}";
    public static final String TEMPLATE_SOCIAL_FB = "\\{\\{image_facebook\\}\\}";
    public static final String TEMPLATE_SOCIAL_TW = "\\{\\{image_twitter\\}\\}";
    public static final String TEMPLATE_SOCIAL_GP = "\\{\\{image_google_plus\\}\\}";
    public static final String TEMPLATE_SOCIAL_LINK = "\\{\\{image_link\\}\\}";

    public static final String APP_URL = "http://timecrypt.co";
    public static final String APP_FACEBOOK = "https://www.facebook.com/timecrypt";
    public static final String APP_TWITTER = "https://twitter.com/timecryptapp";
    public static final String APP_GOOGLE_PLUS = "https://plus.google.com/105664500062213014541";
    // FIXME actually put the link here
    public static final String APP_GIT_HUB = "https://github.com/milosmns";

    public static final String IMAGE_LOGO = "https://i.imgur.com/5pCI2UZ.png";
    public static final String IMAGE_SOCIAL_FB = "https://i.imgur.com/xZp91Fp.png";
    public static final String IMAGE_SOCIAL_TW = "https://i.imgur.com/wJlg7pO.png";
    public static final String IMAGE_SOCIAL_GP = "https://i.imgur.com/EgBEBFX.png";
    public static final String IMAGE_SOCIAL_LINK = "http://i.imgur.com/l7cYPf5.png";

}
