package co.timecrypt.api.v2.servlets;

import co.timecrypt.api.v2.database.TimecryptMessage;
import co.timecrypt.api.v2.definitions.ErrorCode;
import co.timecrypt.api.v2.definitions.JsonResponses;
import co.timecrypt.api.v2.definitions.Parameter;
import co.timecrypt.api.v2.exceptions.InternalException;
import co.timecrypt.api.v2.exceptions.InvalidIdentifierException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet reads a Timecrypt message associated with the provided message ID. Note that reading a message through
 * this decreases view count.
 */
@WebServlet(name = "ReadServlet", description = "Loads info about a Timecrypt message", urlPatterns = {
        "/v2/read"
})
public class ReadServlet extends TimecryptApiServlet {

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = sanitize(request.getParameter(Parameter.ID));
        String password = sanitize(request.getParameter(Parameter.PASSWORD));

        if (id == null) {
            JsonResponses.TimecryptResponse message = new JsonResponses.Error(ErrorCode.MISSING_ID);
            writeToOutput(message, response);
            return;
        }

        JsonResponses.TimecryptResponse message;
        try {
            TimecryptMessage messageData = getDataStore().read(id, password);
            message = new JsonResponses.ReadResponse(messageData.getText(), messageData.getTitle(),
                    messageData.getViewCount(), messageData.getDestructDate());
        } catch (InternalException e) {
            message = new JsonResponses.Error(ErrorCode.INTERNAL);
        } catch (InvalidIdentifierException e) {
            message = new JsonResponses.Error(ErrorCode.MISSING_ID);
        }

        writeToOutput(message, response);
    }

}
