package frontend.servlet;

import frontend.AbstractServlet;
import frontend.annotation.AuthenticationRequired;
import frontend.response.Response;
import frontend.response.SuccessResponse;
import auth.SocialAccountService;
import service.ServiceManager;

import javax.servlet.http.HttpServletRequest;

@AuthenticationRequired
public class SignOutServlet extends AbstractServlet {
    public SignOutServlet(ServiceManager serviceManager, SocialAccountService socialAccountService) {
        super(serviceManager, socialAccountService);
    }

    public Response onPost(HttpServletRequest request) {
        signOutUser(request);
        return new SuccessResponse();
    }

}
