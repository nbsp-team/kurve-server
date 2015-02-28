package frontend.servlet.auth;

import frontend.AbstractAdminServlet;
import frontend.AbstractAuthenticatedServlet;
import frontend.AbstractServlet;
import frontend.response.Response;
import frontend.response.SuccessResponse;
import frontend.response.auth.SignUpResponse;
import frontend.response.error.AuthErrorResponse;
import frontend.response.error.ErrorResponse;
import main.AccountService;
import model.UserProfile;
import org.eclipse.jetty.server.Authentication;

import javax.servlet.http.HttpServletRequest;

public class SignOutServlet extends AbstractAuthenticatedServlet {
    private AccountService accountService;

    public SignOutServlet(AccountService accountService) {
        super(accountService);
        this.accountService = accountService;
    }

    public Response onPost(HttpServletRequest request, UserProfile user) {
        signOutUser(request);
        return new SuccessResponse();
    }

}
