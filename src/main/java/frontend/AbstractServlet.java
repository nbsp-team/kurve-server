package frontend;

import frontend.response.error.ErrorResponse;
import frontend.response.Response;
import model.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * nickolay, 25.02.15.
 */
public abstract class AbstractServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse servResp) throws ServletException, IOException {
        Response response = onGet(req);
        writeResponse(servResp, response);
    }

    protected void doHead(HttpServletRequest req, HttpServletResponse servResp) throws ServletException, IOException {
        Response response = onHead(req);
        writeResponse(servResp, response);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse servResp) throws ServletException, IOException {
        Response response = onPost(req);
        writeResponse(servResp, response);
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse servResp) throws ServletException, IOException {
        Response response = onPut(req);
        writeResponse(servResp, response);
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse servResp) throws ServletException, IOException {
        Response response = onDelete(req);
        writeResponse(servResp, response);
    }

    protected Response onGet(HttpServletRequest req) {
        return null;
    }

    protected Response onHead(HttpServletRequest req) {
        return null;
    }

    protected Response onPost(HttpServletRequest req) {
        return null;
    }

    protected Response onPut(HttpServletRequest req) {
        return null;
    }

    protected Response onDelete(HttpServletRequest req) {
        return null;
    }

    protected void signInUser(HttpServletRequest request, UserProfile user) {
        request.getSession().setAttribute("username", user.getLogin());
    }

    protected void signOutUser(HttpServletRequest request) {
        request.getSession().removeAttribute("username");
    }

    private void writeResponse(HttpServletResponse servResp, Response resp) throws IOException {
        servResp.setContentType("application/json");
        servResp.setHeader("Server", "KurveServer (API v1)");
        if (resp != null) {
            servResp.getWriter().println(resp.getJSON());
        } else {
            writeResponse(servResp,
                    new ErrorResponse(ErrorResponse.ERROR_EMPTY_RESPONSE));
        }
    }
}
