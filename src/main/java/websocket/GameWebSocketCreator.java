package websocket;

import frontend.SessionManager;
import interfaces.AccountService;
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
    private AccountService accountService;
    private SessionManager sessionManager;
    private GameWebSocketHandler.WebSocketMessageListener messageListener;

    public GameWebSocketCreator(SessionManager sessionManager, AccountService accountService, GameWebSocketHandler.WebSocketMessageListener messageListener) {
        this.accountService = accountService;
        this.sessionManager = sessionManager;
        this.messageListener = messageListener;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) {
        String sessionId = getSessionId(servletUpgradeRequest.getCookies());
        return new GameWebSocketHandler(
                getUserBySessionId(sessionId),
                messageListener
        );
    }

    private String getSessionId(List<HttpCookie> cookieList) {
        for(HttpCookie c : cookieList) {
            if (c.getName().equals("JSESSIONID")) {
                return c.getValue();
            }
        }
        return null;
    }

    private UserProfile getUserBySessionId(String sessionId) {
        Optional<HttpSession> session = sessionManager.getSessionById(sessionId);

        if (session.isPresent()) {
            String username = (String) session.get().getAttribute("username");
            return accountService.getUser(username);
        } else {
            return null;
        }
    }
}