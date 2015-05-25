package frontend.servlet;

import frontend.AbstractServlet;
import frontend.response.RatingResponse;
import frontend.response.Response;
import interfaces.SocialAccountService;

import javax.servlet.http.HttpServletRequest;

public class RatingServlet extends AbstractServlet {
    public RatingServlet(SocialAccountService socialAccountService) {
        super(socialAccountService);
    }

    public Response onGet(HttpServletRequest request) {
        return new RatingResponse();
    }
}