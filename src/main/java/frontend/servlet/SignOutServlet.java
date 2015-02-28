package frontend.servlet;

import frontend.AbstractServlet;
import frontend.annotation.ApiMethod;
import frontend.annotation.AuthenticationRequired;
import frontend.response.Response;
import frontend.response.SuccessResponse;
import main.AccountService;

import javax.servlet.http.HttpServletRequest;

@AuthenticationRequired
public class SignOutServlet extends AbstractServlet {
    public SignOutServlet(AccountService accountService) {
        super(accountService);
    }

    @ApiMethod(method = HttpMethod.POST)
    public Response signOut(HttpServletRequest request) {
        signOutUser(request);
        return new SuccessResponse();
    }

}
