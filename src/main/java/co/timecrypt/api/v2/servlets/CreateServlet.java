package co.timecrypt.api.v2.servlets;

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
        getDataStore().create(1, null, null, "lala", "title", "pass");
        //        String id = sanitize(request.getParameter(Parameter.LOCK_CHECK_ID));
        //
        //        if (id == null) {
        //            JsonResponses.TimecryptResponse message = new JsonResponses.Error(ErrorCode.MISSING_ID);
        //            writeToOutput(message, response);
        //            return;
        //        }
        //
        //        try {
        //            boolean hasLock = getDataStore().checkLock(id);
        //            JsonResponses.TimecryptResponse message = new JsonResponses.LockCheckResponse(hasLock);
        //            writeToOutput(message, response);
        //        } catch (InvalidIdentifierException e) {
        //            JsonResponses.TimecryptResponse message = new JsonResponses.Error(ErrorCode.INVALID_ID);
        //            writeToOutput(message, response);
        //        }
    }

}
