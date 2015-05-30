package frontend.servlet;

import frontend.AbstractServlet;
import frontend.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class MobileAuthServlet extends HttpServlet {
    private final SessionManager sessionManager;

    public MobileAuthServlet(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getParameter("hash");
        Optional<HttpSession> httpSession = sessionManager.getSessionById(sessionId);

        if (httpSession.isPresent()) {
            HttpSession session = sessionManager.getSessionById(sessionId).get();
            if (session != null) {
                String userId = (String) session.getAttribute(AbstractServlet.USER_ID_SESSION_ATTRIBUTE);
                req.getSession().setAttribute(AbstractServlet.USER_ID_SESSION_ATTRIBUTE, userId);
                resp.sendRedirect("/#room");
            } else {
                resp.sendRedirect("/");
            }
        } else {
            resp.sendRedirect("/");
        }
    }
}
