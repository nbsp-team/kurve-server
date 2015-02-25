package frontend;

import frontend.response.auth.SignInResponse;
import frontend.response.error.AuthErrorResponse;
import frontend.response.Response;
import main.AccountService;
import model.UserProfile;
import utils.ResponseCodes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class SignInServlet extends AbstractServlet {
    private AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public Response onPost(HttpServletRequest request)  {
        String name = request.getParameter("username");
        String password = request.getParameter("password");

        UserProfile profile = accountService.getUser(name);

        if (profile != null && profile.getPassword().equals(password)) {
            return new SignInResponse(profile);
        } else {
            return new AuthErrorResponse();
        }
    }
}
