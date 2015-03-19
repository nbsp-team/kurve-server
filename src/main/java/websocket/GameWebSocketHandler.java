package websocket;

import game.Game;
import main.AccountService;
import model.UserProfile;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import utils.SessionManager;

import javax.servlet.http.HttpSession;
import java.net.HttpCookie;
import java.util.List;
import java.util.Optional;

/**
 * nickolay, 21.02.15.
 */

public class GameWebSocketHandler extends WebSocketAdapter {
    private WebSocketMessageListener messageListener;
    private UserProfile userProfile;

    public GameWebSocketHandler(UserProfile userProfile, WebSocketMessageListener messageListener) {
        this.userProfile = userProfile;
        this.messageListener = messageListener;
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        System.out.println("Close: statusCode=" + cause.toString());
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println("Close: statusCode=" + statusCode + " " + reason);
        messageListener.onDisconnect(userProfile);
    }

    @Override
    public void onWebSocketText(String message) {
        //TODO: route message to correct messageListener method
    }

    @Override
    public void onWebSocketConnect(Session session) {
        messageListener.onNewConnection(userProfile, new WebSocketConnection(session));
    }

    public static class GameWebSocketCreator implements WebSocketCreator {
        private AccountService accountService;
        private SessionManager sessionManager;
        private WebSocketMessageListener messageListener;

        public GameWebSocketCreator(SessionManager sessionManager, AccountService accountService, WebSocketMessageListener messageListener) {
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

        private static String getSessionId(List<HttpCookie> cookieList) {
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

    public static interface WebSocketMessageListener {
        public void onNewConnection(UserProfile user, WebSocketConnection connection);
        public void onDisconnect(UserProfile user);
        public void onUserReady(UserProfile user);
    }
}