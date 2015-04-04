package frontend.servlet;

import frontend.AbstractServlet;
import frontend.response.AuthErrorResponse;
import frontend.response.Response;
import frontend.response.SignInResponse;
import model.UserProfile;

import javax.servlet.http.HttpServletRequest;

public class SignInServlet extends AbstractServlet {
    public SignInServlet(AccountService accountService) {
        super(accountService);
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
