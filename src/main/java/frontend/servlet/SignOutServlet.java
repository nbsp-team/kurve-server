package frontend.servlet;

import frontend.AbstractServlet;
import frontend.annotation.AuthenticationRequired;
import frontend.response.Response;
import frontend.response.SuccessResponse;
import interfaces.AccountService;

import javax.servlet.http.HttpServletRequest;

@AuthenticationRequired
public class SignOutServlet extends AbstractServlet {
    public SignOutServlet(AccountService accountService) {
        super(accountService);
    }

    public Response onPost(HttpServletRequest request) {
        signOutUser(request);
        return new SuccessResponse();
    }

}
