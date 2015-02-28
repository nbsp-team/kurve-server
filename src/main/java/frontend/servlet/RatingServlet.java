package frontend.servlet;

import frontend.AbstractServlet;
import frontend.annotation.ApiMethod;
import frontend.response.RatingResponse;
import frontend.response.Response;
import main.AccountService;

import javax.servlet.http.HttpServletRequest;

public class RatingServlet extends AbstractServlet {
    public RatingServlet(AccountService accountService) {
        super(accountService);
    }

    @ApiMethod
    public Response getGlobalRating(HttpServletRequest request) {
        return new RatingResponse();
    }
}