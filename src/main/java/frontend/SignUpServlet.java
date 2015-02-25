package frontend;

import frontend.response.Response;
import frontend.response.auth.SignUpResponse;
import frontend.response.error.AuthErrorResponse;
import frontend.response.error.ErrorResponse;
import main.AccountService;
import model.UserProfile;

import javax.servlet.http.HttpServletRequest;

public class SignUpServlet extends AbstractServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public Response onPost(HttpServletRequest request) {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if(username == null || email == null || password == null) {
            return new ErrorResponse(ErrorResponse.ERROR_INVALID_PARAMS);
        } else if (!accountService.addUser(username, new UserProfile(username, password, email))) {
            return new AuthErrorResponse("User with name: " + username + " already exists");
        } else {
            return new SignUpResponse(accountService.getUser(username));
        }
    }

}
