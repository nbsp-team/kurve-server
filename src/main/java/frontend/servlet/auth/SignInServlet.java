package frontend.servlet.auth;

import frontend.AbstractServlet;
import frontend.response.Response;
import frontend.response.auth.SignInResponse;
import frontend.response.error.AuthErrorResponse;
import main.AccountService;
import model.UserProfile;

import javax.servlet.http.HttpServletRequest;

public class SignInServlet extends AbstractServlet {
    private AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public Response onPost(HttpServletRequest request)  {
        String name = request.getParameter("username");
        String password = request.getParameter("password");

        UserProfile user = accountService.getUser(name);

        if (user != null && user.getPassword().equals(password)) {
            signInUser(request, user);
            return new SignInResponse(user);
        } else {
            return new AuthErrorResponse();
        }
    }
}
