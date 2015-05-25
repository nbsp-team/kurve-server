package frontend.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import frontend.AbstractServlet;
import frontend.response.AuthErrorResponse;
import frontend.response.Response;
import frontend.response.SignInResponse;
import interfaces.SocialAccountService;
import jdk.nashorn.internal.parser.JSONParser;
import model.UserProfile;
import utils.SocialAuth;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SocialSignInServlet extends HttpServlet {
    SocialAccountService socialAccountService;

    public SocialSignInServlet(SocialAccountService socialAccountService) {
        this.socialAccountService = socialAccountService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int authProviderIndex = Integer.valueOf(req.getParameter("type"));
        String code = req.getParameter("code");

        if (authProviderIndex < 0 | authProviderIndex >= SocialAuth.AuthProvider.values().length) {
            resp.sendRedirect("/#login");
        }

        UserProfile user = SocialAuth.auth(
                SocialAuth.AuthProvider.values()[authProviderIndex],
                code
        );

        socialAccountService.addUser(user);

        if (user != null) {
            req.getSession().setAttribute(AbstractServlet.USER_ID_SESSION_ATTRIBUTE, user.getId());
            resp.getWriter().write("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<script>\n" +
                    "\twindow.opener.onSocialAuth(true);\n" +
                    "\twindow.close();\n" +
                    "</script>\n" +
                    "</head>\n" +
                    "<body></body>\n" +
                    "</html>");
        } else {
            resp.getWriter().write("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<script>\n" +
                    "\twindow.opener.onSocialAuth(false);\n" +
                    "\twindow.close();\n" +
                    "</script>\n" +
                    "</head>\n" +
                    "<body></body>\n" +
                    "</html>");
        }
    }
}
