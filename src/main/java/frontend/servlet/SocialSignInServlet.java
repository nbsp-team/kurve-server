package frontend.servlet;

import auth.SocialAccountService;
import frontend.AbstractServlet;
import frontend.FrontendService;
import model.UserProfile;
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
    FrontendService frontendService;

    public SocialSignInServlet(FrontendService frontendService, SocialAccountService socialAccountService) {
        this.socialAccountService = socialAccountService;
        this.frontendService = frontendService;
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

        user = frontendService.addUser(user);

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("authSuccess", user != null ? "true" : "false");

        if (user != null) {
            req.getSession().setAttribute(AbstractServlet.USER_ID_SESSION_ATTRIBUTE, user.getId());
        }

        resp.getWriter().write(PageGenerator.getPage("social_signin_popup.html", pageVariables));
    }
}
