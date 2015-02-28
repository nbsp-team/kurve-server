package frontend.servlet;

import frontend.AbstractServlet;
import frontend.annotation.ApiMethod;
import frontend.response.Response;
import frontend.response.SignInResponse;
import frontend.response.AuthErrorResponse;
import main.AccountService;
import model.UserProfile;

import javax.servlet.http.HttpServletRequest;

public class SignInServlet extends AbstractServlet {
    public SignInServlet(AccountService accountService) {
        super(accountService);
    }

    @ApiMethod(method = HttpMethod.POST)
    public Response signIn(HttpServletRequest request)  {
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
