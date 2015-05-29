package frontend.servlet;

import frontend.AbstractServlet;
import frontend.response.RatingResponse;
import frontend.response.Response;
import auth.SocialAccountService;
import service.ServiceManager;

import javax.servlet.http.HttpServletRequest;

public class RatingServlet extends AbstractServlet {
    public RatingServlet(ServiceManager serviceManager, SocialAccountService socialAccountService) {
        super(serviceManager, socialAccountService);
    }

    public Response onGet(HttpServletRequest request) {
        return new RatingResponse();
    }
}