package frontend.servlet.user;

import frontend.AbstractAuthenticatedServlet;
import frontend.AbstractServlet;
import frontend.response.Response;
import frontend.response.auth.SignUpResponse;
import frontend.response.error.AuthErrorResponse;
import frontend.response.error.ErrorResponse;
import frontend.response.user.GetUserResponse;
import main.AccountService;
import model.UserProfile;

import javax.servlet.http.HttpServletRequest;

public class UserServlet extends AbstractAuthenticatedServlet {
    public UserServlet(AccountService accountService) {
        super(accountService);
    }

    public Response onGet(HttpServletRequest request, UserProfile user) {
        return new GetUserResponse(user);
    }
}
