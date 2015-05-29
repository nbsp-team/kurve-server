package frontend.servlet;

import frontend.AbstractServlet;
import auth.SocialAccountService;
import model.UserProfile;
import service.*;
import utils.Bundle;
import utils.PageGenerator;
import utils.SocialAuthHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SocialSignInServlet extends HttpServlet {
    SocialAccountService socialAccountService;
    ServiceManager serviceManager;

    public SocialSignInServlet(ServiceManager serviceManager, SocialAccountService socialAccountService) {
        this.socialAccountService = socialAccountService;
        this.serviceManager = serviceManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int authProviderIndex = Integer.valueOf(req.getParameter("type"));
        String code = req.getParameter("code");

        if (authProviderIndex < 0 | authProviderIndex >= SocialAuthHelper.AuthProvider.values().length) {
            resp.sendRedirect("/#login");
        }

        UserProfile user = SocialAuthHelper.auth(
                SocialAuthHelper.AuthProvider.values()[authProviderIndex],
                code
        );

        Bundle args = new Bundle();
        args.putSerializable("user", user);
        Request request = new Request("add_user", args, true);
        serviceManager.process(
                ServiceType.ACCOUNT_SERVICE,
                req.getSession().getId().hashCode(),
                request
        );
        Response response = serviceManager.waitResponse(request);
        user = (UserProfile) response.getArgs().getSerializable("user");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("authSuccess", user != null ? "true" : "false");

        if (user != null) {
            req.getSession().setAttribute(AbstractServlet.USER_ID_SESSION_ATTRIBUTE, user.getId());
        }

        resp.getWriter().write(PageGenerator.getPage("social_signin_popup.html", pageVariables));
    }
}
