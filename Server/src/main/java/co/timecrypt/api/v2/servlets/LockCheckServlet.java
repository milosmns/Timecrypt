package co.timecrypt.api.v2.servlets;

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
 * This servlet checks whether a Timecrypt message is locked with a password or not.
 */
@WebServlet(name = "LockCheckServlet", description = "Checks if message is locked", urlPatterns = {
        "/v2/is-locked"
})
public class LockCheckServlet extends TimecryptApiServlet {

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = sanitize(request.getParameter(Parameter.ID));

        if (id == null) {
            JsonResponses.TimecryptResponse message = new JsonResponses.Error(ErrorCode.MISSING_ID);
            writeToOutput(message, response);
            return;
        }

        JsonResponses.TimecryptResponse message;
        try {
            boolean hasLock = getDataStore().checkLock(id);
            message = new JsonResponses.LockCheckResponse(hasLock);
        } catch (InvalidIdentifierException e) {
            message = new JsonResponses.Error(ErrorCode.INVALID_ID);
        } catch (InternalException e) {
            message = new JsonResponses.Error(ErrorCode.INTERNAL);
        }

        writeToOutput(message, response);
    }

}
