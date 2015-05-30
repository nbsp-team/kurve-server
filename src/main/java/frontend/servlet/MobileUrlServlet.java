package frontend.servlet;

import auth.SocialAccountService;
import frontend.AbstractServlet;
import frontend.response.GetMobileUrlResponse;
import frontend.response.Response;

import javax.servlet.http.HttpServletRequest;

public class MobileUrlServlet extends AbstractServlet {
    public MobileUrlServlet(SocialAccountService socialAccountService) {
        super(socialAccountService);
    }

    public Response onGet(HttpServletRequest request) {
        return new GetMobileUrlResponse(request.getSession().getId());
    }
}
