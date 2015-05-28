package frontend;

import frontend.annotation.AdminRightsRequired;
import frontend.annotation.AuthenticationRequired;
import frontend.response.ErrorResponse;
import frontend.response.PermissionDeniedErrorResponse;
import frontend.response.Response;
import interfaces.SocialAccountService;
import model.UserProfile;
import service.ServiceManager;
import service.ServiceType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * nickolay, 25.02.15.
 */

public abstract class AbstractServlet extends HttpServlet {
    public static final String USER_ID_SESSION_ATTRIBUTE = "user_id";
    protected SocialAccountService socialAccountService;

    public AbstractServlet(SocialAccountService socialAccountService) {
        this.socialAccountService = socialAccountService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!checkPermissions(request, response)) {
            writeResponse(response, new PermissionDeniedErrorResponse());
        } else {
            writeResponse(response, onGet(request));
        }
    }

    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!checkPermissions(request, response)) {
            writeResponse(response, new PermissionDeniedErrorResponse());
        } else {
            writeResponse(response, onHead(request));
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!checkPermissions(request, response)) {
            writeResponse(response, new PermissionDeniedErrorResponse());
        } else {
            writeResponse(response, onPost(request));
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!checkPermissions(request, response)) {
            writeResponse(response, new PermissionDeniedErrorResponse());
        } else {
            writeResponse(response, onPut(request));
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!checkPermissions(request, response)) {
            writeResponse(response, new PermissionDeniedErrorResponse());
        } else {
            writeResponse(response, onDelete(request));
        }
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


    private boolean checkPermissions(HttpServletRequest request, HttpServletResponse response) {
        if (this.getClass().isAnnotationPresent(AuthenticationRequired.class)) {
            if (!isAuthenticated(request)) {
                return false;
            }
        }

        if (this.getClass().isAnnotationPresent(AdminRightsRequired.class)) {
            if (!isAuthenticated(request) || !getUser(request).isAdmin()) {
                return false;
            }
        }

        return true;
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        return request.getSession().getAttribute(USER_ID_SESSION_ATTRIBUTE) != null;
    }

    private void writeResponse(HttpServletResponse response, Response apiResponse) {
        response.setContentType(Response.getContentType());
        response.setHeader("Server", "KurveServer (API v1)");

        if (apiResponse != null) {
            try {
                response.getWriter().println(apiResponse.getBody());
            } catch (IOException e) {
                writeResponse(response,
                        new ErrorResponse(ErrorResponse.ErrorResponseCode.ERROR_INTERNAL_SERVER));
                e.printStackTrace();
            }
        } else {
            writeResponse(response,
                    new ErrorResponse(ErrorResponse.ErrorResponseCode.ERROR_EMPTY_RESPONSE));
        }
    }

    /* Authenticated required */

    protected UserProfile getUser(HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute(USER_ID_SESSION_ATTRIBUTE);
        return socialAccountService.getUserById(userId);
    }

    protected void signInUser(HttpServletRequest request, UserProfile user) {
        request.getSession().setAttribute(USER_ID_SESSION_ATTRIBUTE, user.getId());
    }

    protected void signOutUser(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_ID_SESSION_ATTRIBUTE);
    }
}
