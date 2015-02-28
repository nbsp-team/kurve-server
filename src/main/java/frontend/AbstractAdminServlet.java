package frontend;

import frontend.response.Response;
import frontend.response.error.ErrorResponse;
import frontend.response.error.PermissionDeniedErrorResponse;
import main.AccountService;
import model.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * nickolay, 25.02.15.
 */
public abstract class AbstractAdminServlet extends AbstractAuthenticatedServlet {
    public AbstractAdminServlet(AccountService accountService) {
        super(accountService);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse servResp) throws ServletException, IOException {
        UserProfile user = getUser(req);

        if (user != null && user.isAdmin()) {
            Response response = onGet(req, user);
            writeResponse(servResp, response);
        } else {
            writeResponse(servResp, new PermissionDeniedErrorResponse());
        }
    }

    protected void doHead(HttpServletRequest req, HttpServletResponse servResp) throws ServletException, IOException {
        UserProfile user = getUser(req);

        if (user != null && user.isAdmin()) {
            Response response = onHead(req, user);
            writeResponse(servResp, response);
        } else {
            writeResponse(servResp, new PermissionDeniedErrorResponse());
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse servResp) throws ServletException, IOException {
        UserProfile user = getUser(req);

        if (user != null && user.isAdmin()) {
            Response response = onPost(req, user);
            writeResponse(servResp, response);
        } else {
            writeResponse(servResp, new PermissionDeniedErrorResponse());
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse servResp) throws ServletException, IOException {
        UserProfile user = getUser(req);

        if (user != null && user.isAdmin()) {
            Response response = onPut(req, user);
            writeResponse(servResp, response);
        } else {
            writeResponse(servResp, new PermissionDeniedErrorResponse());
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse servResp) throws ServletException, IOException {
        UserProfile user = getUser(req);

        if (user != null && user.isAdmin()) {
            Response response = onDelete(req, user);
            writeResponse(servResp, response);
        } else {
            writeResponse(servResp, new PermissionDeniedErrorResponse());
        }
    }

    protected Response onGet(HttpServletRequest req, UserProfile user) {
        return null;
    }

    protected Response onHead(HttpServletRequest req, UserProfile user) {
        return null;
    }

    protected Response onPost(HttpServletRequest req, UserProfile user) {
        return null;
    }

    protected Response onPut(HttpServletRequest req, UserProfile user) {
        return null;
    }

    protected Response onDelete(HttpServletRequest req, UserProfile user) {
        return null;
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

    private UserProfile getUser(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        return accountService.getUser(username);
    }
}
