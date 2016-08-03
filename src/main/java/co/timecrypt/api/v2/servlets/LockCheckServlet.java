
package co.timecrypt.api.v2.servlets;

import com.sun.media.sound.InvalidDataException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet checks whether a Timecrypt message is locked with a password or not.
 */
@WebServlet(name = "LockCheckServlet", description = "Checks if message is locked", urlPatterns = {
        "/v2/is-locked"
})
public class LockCheckServlet extends TimecryptApiServlet {

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        // TODO find the ID
        String id = "0";

        try {
            boolean hasLock = getDataStore().checkLock(id);
            // TODO generate JSON
        } catch (InvalidDataException ignored) {
            // this means no such ID
            // TODO generate JSON
        }
    }

}
