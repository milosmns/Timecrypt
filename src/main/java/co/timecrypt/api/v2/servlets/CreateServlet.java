package co.timecrypt.api.v2.servlets;

import co.timecrypt.api.v2.definitions.ErrorCode;
import co.timecrypt.api.v2.definitions.JsonResponses;
import co.timecrypt.api.v2.definitions.Parameter;
import co.timecrypt.api.v2.exceptions.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet creates a new Timecrypt message using the provided data.
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

        JsonResponses.TimecryptResponse message;
        try {
            String textId = getDataStore().create(viewCount, destructDate, emailTo, emailFrom, text, title, password);
            message = new JsonResponses.CreateResponse(textId);
        } catch (InvalidTitleException e) {
            message = new JsonResponses.Error(ErrorCode.INVALID_TITLE);
        } catch (InvalidTextException e) {
            message = new JsonResponses.Error(ErrorCode.INVALID_TEXT);
        } catch (InvalidViewCountException e) {
            message = new JsonResponses.Error(ErrorCode.INVALID_VIEW_COUNT);
        } catch (InvalidPasswordException e) {
            message = new JsonResponses.Error(ErrorCode.INVALID_PASSWORD);
        } catch (InvalidDestructDateException e) {
            message = new JsonResponses.Error(ErrorCode.INVALID_DESTRUCT_DATE);
        } catch (InvalidEmailException e) {
            if (e.getType() == InvalidEmailException.Type.TO) {
                message = new JsonResponses.Error(ErrorCode.INVALID_EMAIL_TO);
            } else {
                message = new JsonResponses.Error(ErrorCode.INVALID_EMAIL_FROM);
            }
        } catch (InternalException e) {
            message = new JsonResponses.Error(ErrorCode.INTERNAL);
        }

        writeToOutput(message, response);

    }

}
