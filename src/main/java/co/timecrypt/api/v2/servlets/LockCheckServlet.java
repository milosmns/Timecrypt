
package co.timecrypt.api.v2.servlets;

import co.timecrypt.api.v2.utils.Config;
import com.sun.media.sound.InvalidDataException;

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
        String id = sanitize(request.getParameter(Config.PARAM_LOCK_CHECK_ID));

        // FIXME use proper JSON and mime type
        if (id == null) {
            response.getWriter().print("ID is null");
            return;
        }

        try {
            // TODO generate JSON
            boolean hasLock = getDataStore().checkLock(id);
            response.getWriter().print("Data is " + (hasLock ? "locked" : "unlocked"));
        } catch (InvalidDataException ignored) {
            // this means no such ID
            // TODO generate JSON
            response.getWriter().print("ID is unknown");
        }
    }

}
