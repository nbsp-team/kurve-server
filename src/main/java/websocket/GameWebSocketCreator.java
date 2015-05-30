package websocket;

import auth.SocialAccountService;
import frontend.AbstractServlet;
import frontend.SessionManager;
import model.UserProfile;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import javax.servlet.http.HttpSession;
import java.net.HttpCookie;
import java.util.List;
import java.util.Optional;

/**
 * nickolay, 18.04.15.
 */

public class GameWebSocketCreator implements WebSocketCreator {
    private SocialAccountService socialAccountService;
    private SessionManager sessionManager;
    private GameWebSocketHandler.WebSocketMessageListener messageListener;

    public GameWebSocketCreator(SessionManager sessionManager, SocialAccountService socialAccountService,
                                GameWebSocketHandler.WebSocketMessageListener messageListener) {
        this.socialAccountService = socialAccountService;
        this.sessionManager = sessionManager;
        this.messageListener = messageListener;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest,
                                  ServletUpgradeResponse servletUpgradeResponse) {
        String sessionId = getSessionId(servletUpgradeRequest.getCookies());
        return new GameWebSocketHandler(
                getUserBySessionId(sessionId),
                messageListener
        );
    }

    private String getSessionId(List<HttpCookie> cookieList) {
        for (HttpCookie c : cookieList) {
            if (c.getName().equals("JSESSIONID")) {
                return c.getValue();
            }
        }
        return null;
    }

    private UserProfile getUserBySessionId(String sessionId) {
        Optional<HttpSession> session = sessionManager.getSessionById(sessionId);

        if (session.isPresent()) {
            String userId = (String) session.get().getAttribute(AbstractServlet.USER_ID_SESSION_ATTRIBUTE);
            if (userId != null) {
                return socialAccountService.getUserById(userId);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}