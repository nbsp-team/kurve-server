package frontend.servlet;

import frontend.AbstractServlet;
import frontend.annotation.AuthenticationRequired;
import frontend.response.AuthErrorResponse;
import frontend.response.GetUserResponse;
import frontend.response.Response;
import auth.SocialAccountService;
import model.UserProfile;
import service.ServiceManager;

import javax.servlet.http.HttpServletRequest;

@AuthenticationRequired
public class UserServlet extends AbstractServlet {
    public UserServlet(ServiceManager serviceManager, SocialAccountService socialAccountService) {
        super(serviceManager, socialAccountService);
    }

    public Response onGet(HttpServletRequest request) {
        UserProfile user = getUser(request);

        if (user != null) {
            return new GetUserResponse(user);
        } else {
            return new AuthErrorResponse();
        }
    }
}
