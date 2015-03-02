package frontend.servlet;

import frontend.AbstractServlet;
import frontend.annotation.AuthenticationRequired;
import frontend.response.GetUserResponse;
import frontend.response.Response;
import main.AccountService;

import javax.servlet.http.HttpServletRequest;

@AuthenticationRequired
public class UserServlet extends AbstractServlet {
    public UserServlet(AccountService accountService) {
        super(accountService);
    }

    public Response onGet(HttpServletRequest request) {
        return new GetUserResponse(getUser(request));
    }
}
