package frontend.servlet;

import frontend.AbstractServlet;
import frontend.response.Response;
import frontend.response.SignUpResponse;
import frontend.response.AuthErrorResponse;
import frontend.response.ErrorResponse;
import main.AccountService;
import model.UserProfile;

import javax.servlet.http.HttpServletRequest;

public class SignUpServlet extends AbstractServlet {
    public SignUpServlet(AccountService accountService) {
        super(accountService);
    }

    public Response onPost(HttpServletRequest request) {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if(username == null || email == null || password == null) {
            return new ErrorResponse(ErrorResponse.ERROR_INTERNAL_SERVER);
        } else if (!accountService.addUser(username, new UserProfile(username, password, email))) {
            return new AuthErrorResponse("User with name: " + username + " already exists");
        } else {
            UserProfile user = accountService.getUser(username);
            signInUser(request, user);
            return new SignUpResponse(user);
        }
    }

}