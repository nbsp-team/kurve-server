package frontend;

import frontend.annotation.AdminRightsRequired;
import frontend.annotation.ApiMethod;
import frontend.annotation.AuthenticationRequired;
import frontend.response.ErrorResponse;
import frontend.response.PermissionDeniedErrorResponse;
import frontend.response.Response;
import main.AccountService;
import model.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * nickolay, 25.02.15.
 */

public abstract class AbstractServlet extends HttpServlet {
    protected AccountService accountService;

    public AbstractServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public enum HttpMethod {
        GET, HEAD, POST, PUT, DELETE
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sendToHandler(request, response, HttpMethod.GET);
    }

    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sendToHandler(request, response, HttpMethod.HEAD);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sendToHandler(request, response, HttpMethod.POST);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sendToHandler(request, response, HttpMethod.PUT);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sendToHandler(request, response, HttpMethod.DELETE);
    }


    private void sendToHandler(HttpServletRequest request, HttpServletResponse response, HttpMethod method) {
        if (this.getClass().isAnnotationPresent(AuthenticationRequired.class)) {
            if (!isAuthenticated(request)) {
                writeResponse(response, new PermissionDeniedErrorResponse());
                return;
            }
        }

        if (this.getClass().isAnnotationPresent(AdminRightsRequired.class)) {
            if (!isAuthenticated(request) || !getUser(request).isAdmin()) {
                writeResponse(response, new PermissionDeniedErrorResponse());
                return;
            }
        }

        Method[] methods = this.getClass().getMethods();
        for(Method m : methods) {
            if (m.isAnnotationPresent(ApiMethod.class)) {
                ApiMethod apiMethod = m.getDeclaredAnnotation(ApiMethod.class);
                if (apiMethod.method() == method) {
                    processHandler(m, request, response);
                    return;
                }
            }
        }
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        return request.getSession().getAttribute("username") != null;
    }

    private void processHandler(Method handler, HttpServletRequest request, HttpServletResponse response) {
        try {
            Response apiResponse = (Response) handler.invoke(this, request);
            writeResponse(response, apiResponse);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void writeResponse(HttpServletResponse response, Response apiResponse) {
        response.setContentType("application/json");
        response.setHeader("Server", "KurveServer (API v1)");

        if (apiResponse != null) {
            try {
                response.getWriter().println(apiResponse.getJSON());
            } catch (IOException e) {
                writeResponse(response,
                        new ErrorResponse(ErrorResponse.ERROR_INTERNAL_SERVER));
                e.printStackTrace();
            }
        } else {
            writeResponse(response,
                    new ErrorResponse(ErrorResponse.ERROR_EMPTY_RESPONSE));
        }
    }


    /* Authenticated required */

    protected UserProfile getUser(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        return accountService.getUser(username);
    }

    protected void signInUser(HttpServletRequest request, UserProfile user) {
        request.getSession().setAttribute("username", user.getLogin());
    }

    protected void signOutUser(HttpServletRequest request) {
        request.getSession().removeAttribute("username");
    }
}
