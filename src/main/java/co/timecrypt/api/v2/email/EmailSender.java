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
     * @param to   Who to send the email to. Must be a valid email address
     * @param text What text to send over to the destination email
     * @return {@code True} if all goes well, {@code false} if the email fails to send
     */
    public boolean send(String to, String text) {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(EmailConfig.PROP_API, key));
        WebResource webResource = client.resource(EmailConfig.API_URL);
        MultivaluedMapImpl formData = new MultivaluedMapImpl();

        formData.add(EmailConfig.PROP_TEXT, text);
        formData.add(EmailConfig.PROP_FROM, EmailConfig.EMAIL_FROM_NAME + " " + EmailConfig.EMAIL_FROM_ADDR);
        formData.add(EmailConfig.PROP_TO, to);
        formData.add(EmailConfig.PROP_SUBJECT, EmailConfig.EMAIL_SUBJECT);
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
        client.destroy();
        return response.getStatus() == 200;
    }

}
