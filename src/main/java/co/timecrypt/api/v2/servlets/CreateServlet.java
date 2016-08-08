package co.timecrypt.api.v2.servlets;

import co.timecrypt.api.v2.definitions.ErrorCode;
import co.timecrypt.api.v2.definitions.JsonResponses;
import co.timecrypt.api.v2.definitions.Parameter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet checks whether a Timecrypt message is locked with a password or not.
 */
@WebServlet(name = "CreateServlet", description = "Creates a new Timecrypt message", urlPatterns = {
        "/v2/create"
})
public class CreateServlet extends TimecryptApiServlet {

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String text = sanitize(request.getParameter(Parameter.CREATE_TEXT));

        if (text == null) {
            JsonResponses.TimecryptResponse message = new JsonResponses.Error(ErrorCode.MISSING_TEXT);
            writeToOutput(message, response);
            return;
        }

        String viewCount = sanitize(request.getParameter(Parameter.CREATE_VIEW_COUNT));
        String destructDate = sanitize(request.getParameter(Parameter.CREATE_DESTRUCT_DATE));
        String emailTo = sanitize(request.getParameter(Parameter.CREATE_EMAIL_TO));
        String emailFrom = sanitize(request.getParameter(Parameter.CREATE_EMAIL_FROM));
        String title = sanitize(request.getParameter(Parameter.CREATE_TITLE));
        String password = sanitize(request.getParameter(Parameter.CREATE_PASSWORD));

        String messageId = getDataStore().create(viewCount, destructDate, emailTo, emailFrom, text, title, password);
        JsonResponses.TimecryptResponse message = new JsonResponses.CreateResponse(messageId);
        writeToOutput(message, response);
    }

}
