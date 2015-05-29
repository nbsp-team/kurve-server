package frontend.servlet;

import auth.SocialAccountService;
import frontend.AbstractServlet;
import frontend.annotation.AuthenticationRequired;
import frontend.response.Response;
import frontend.response.SuccessResponse;

import javax.servlet.http.HttpServletRequest;

@AuthenticationRequired
public class SignOutServlet extends AbstractServlet {
    public SignOutServlet(SocialAccountService socialAccountService) {
        super(socialAccountService);
    }

    public Response onPost(HttpServletRequest request) {
        signOutUser(request);
        return new SuccessResponse();
    }

}
