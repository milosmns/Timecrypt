
package com.timecrypt.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Prints anything you throw at it.
 */
@WebServlet(name = "EchoServlet", displayName = "Echo", urlPatterns = {
        "/echo"
})
public class EchoServlet extends HttpServlet {

    public static final int TYPE_GET = 0;
    public static final int TYPE_POST = 1;
    public static final int TYPE_PUT = 2;
    public static final int TYPE_DELETE = 3;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleEcho(TYPE_POST, request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleEcho(TYPE_GET, request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleEcho(TYPE_PUT, request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleEcho(TYPE_DELETE, request, response);
    }

    /**
     * Does the echo request handling.
     * 
     * @param type Which type is this request
     * @param request Handling this request
     * @param response Using this response
     * @throws ServletException If something goes wrong
     * @throws IOException If something goes wrong
     */
    private void handleEcho(int type, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String typeName = getTypeName(type);
        response.getOutputStream().print(typeName + ": " + request.toString());
    }

    /**
     * Converts integer type to String.
     * 
     * @param type Which type to convert
     * @return A String, display name
     */
    private String getTypeName(int type) {
        switch (type) {
            case TYPE_GET:
                return "GET";
            case TYPE_POST:
                return "POST";
            case TYPE_PUT:
                return "PUT";
            case TYPE_DELETE:
                return "DELETE";
            default:
                return "UNKNOWN";
        }
    }

}
