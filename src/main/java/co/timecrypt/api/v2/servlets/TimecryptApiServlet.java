
package co.timecrypt.api.v2.servlets;

import co.timecrypt.api.v2.database.TimecryptDataStore;
import co.timecrypt.api.v2.database.postgresql.PostgresProvider;
import co.timecrypt.api.v2.definitions.JsonResponses;
import co.timecrypt.utils.TextUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The base class for all Timecrypt servlets. This handles data store initialization, destruction and other stuff.
 */
public abstract class TimecryptApiServlet extends HttpServlet {

    private TimecryptDataStore dataStore;

    @Override
    public void init() throws ServletException {
        super.init();
        PostgresProvider.onServletInitialized(this);
        dataStore = PostgresProvider.getDataStore(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        PostgresProvider.onServletDestroyed(this);
        dataStore = null;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isPostAllowed()) {
            handleRequest(req, resp);
        } else {
            super.doPost(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isGetAllowed()) {
            handleRequest(req, resp);
        } else {
            super.doGet(req, resp);
        }
    }

    /**
     * Sanitizes the given request parameter.
     *
     * @param param Which parameter to sanitize
     * @return {@code Null} if the given parameter {@link TextUtils#isEmpty(String)}, or trimmed value if not empty
     */
    protected final String sanitize(String param) {
        return TextUtils.isEmpty(param) ? null : param.trim();
    }

    /**
     * Writes the given {@link co.timecrypt.api.v2.definitions.JsonResponses.TimecryptResponse} raw message to the given
     * {@link HttpServletResponse} output, using JSON message format.
     *
     * @param raw      The raw message, in Java format
     * @param response Where to write the JSON to
     * @throws IOException If something goes wrong with the response output stream
     */
    protected final void writeToOutput(JsonResponses.TimecryptResponse raw, HttpServletResponse response) throws IOException {
        String json = new Gson().toJson(raw);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    /**
     * @return This servlet's data store
     */
    protected TimecryptDataStore getDataStore() {
        return dataStore;
    }

    /**
     * This handles the request for both {@link #doPost(HttpServletRequest, HttpServletResponse)} and
     * {@link #doGet(HttpServletRequest, HttpServletResponse)}. If you don't want to handle any of these, override {@link #isPostAllowed()}
     * or {@link #isGetAllowed()}, respectively.
     *
     * @param request  Which request to handle
     * @param response Which response to use for the reaction
     */
    abstract protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    /**
     * @return {@code True} if {@link #doPost(HttpServletRequest, HttpServletResponse)} is allowed to be handled by
     * {@link #handleRequest(HttpServletRequest, HttpServletResponse)}, {@code false} if not
     */
    protected boolean isPostAllowed() {
        return true;
    }

    /**
     * @return {@code True} if {@link #doGet(HttpServletRequest, HttpServletResponse)} is allowed to be handled by
     * {@link #handleRequest(HttpServletRequest, HttpServletResponse)}, {@code false} if not
     */
    protected boolean isGetAllowed() {
        return true;
    }

}
