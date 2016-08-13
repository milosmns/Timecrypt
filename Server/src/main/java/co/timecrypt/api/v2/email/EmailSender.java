package co.timecrypt.api.v2.email;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MediaType;

/**
 * A basic utility class for sending email messages using the MailGun API.
 */
public class EmailSender {

    private final String key;

    public EmailSender(String key) {
        this.key = key;
    }

    /**
     * Sends an email to the given address.
     *
     * @param to      Who to send the email to. Must be a valid email address
     * @param content What text to send over to the destination email
     * @param isHtml  Whether the text provided is a valid HTML document
     * @return {@code True} if all goes well, {@code false} if the email fails to send
     */
    public boolean send(String to, String content, boolean isHtml) {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(EmailConfig.PROP_API, key));
        WebResource webResource = client.resource(EmailConfig.API_URL);
        MultivaluedMapImpl formData = new MultivaluedMapImpl();

        if (isHtml) {
            formData.add(EmailConfig.PROP_HTML, content);
        } else {
            formData.add(EmailConfig.PROP_TEXT, content);
        }
        formData.add(EmailConfig.PROP_FROM, EmailConfig.EMAIL_FROM_NAME + " " + EmailConfig.EMAIL_FROM_ADDR);
        formData.add(EmailConfig.PROP_TO, to);
        formData.add(EmailConfig.PROP_SUBJECT, EmailConfig.EMAIL_SUBJECT);
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
        client.destroy();
        return response.getStatus() == 200;
    }

    /**
     * Fills in the email HTML template and prepares it for sending.
     *
     * @param htmlTemplate The HTML template content
     * @param id           The Timecrypt message ID
     * @param destructDate Self-destruct date of the message
     * @param viewCount    The total allowed view count for the message
     * @return The prepared/filled template
     */
    public String prepare(String htmlTemplate, String id, String destructDate, String viewCount) {
        String prepared = htmlTemplate.trim();

        // replace all app and URL placeholders first
        prepared = prepared.replaceAll(EmailConfig.TEMPLATE_URL, EmailConfig.APP_URL);
        prepared = prepared.replaceAll(EmailConfig.TEMPLATE_FACEBOOK, EmailConfig.APP_FACEBOOK);
        prepared = prepared.replaceAll(EmailConfig.TEMPLATE_TWITTER, EmailConfig.APP_TWITTER);
        prepared = prepared.replaceAll(EmailConfig.TEMPLATE_GOOGLE_PLUS, EmailConfig.APP_GOOGLE_PLUS);
        prepared = prepared.replaceAll(EmailConfig.TEMPLATE_GIT_HUB, EmailConfig.APP_GIT_HUB);

        // now replace all image placeholders
        prepared = prepared.replaceAll(EmailConfig.TEMPLATE_LOGO, EmailConfig.IMAGE_LOGO);
        prepared = prepared.replaceAll(EmailConfig.TEMPLATE_SOCIAL_FB, EmailConfig.IMAGE_SOCIAL_FB);
        prepared = prepared.replaceAll(EmailConfig.TEMPLATE_SOCIAL_TW, EmailConfig.IMAGE_SOCIAL_TW);
        prepared = prepared.replaceAll(EmailConfig.TEMPLATE_SOCIAL_GP, EmailConfig.IMAGE_SOCIAL_GP);
        prepared = prepared.replaceAll(EmailConfig.TEMPLATE_SOCIAL_LINK, EmailConfig.IMAGE_SOCIAL_LINK);

        // and now fill in the stuff for the message
        prepared = prepared.replaceAll(EmailConfig.TEMPLATE_MESSAGE_ID, id);
        prepared = prepared.replaceAll(EmailConfig.TEMPLATE_SELF_DESTRUCT, destructDate);
        prepared = prepared.replaceAll(EmailConfig.TEMPLATE_VIEW_COUNT, viewCount);

        return prepared;
    }

}
