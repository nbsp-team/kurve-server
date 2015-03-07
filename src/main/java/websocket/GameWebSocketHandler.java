package websocket;

import game.Game;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */

public class GameWebSocketHandler extends WebSocketAdapter {
    private Game game;

    public GameWebSocketHandler(Game gameInstance) {
        this.game = gameInstance;

    }

    @Override
    public void onWebSocketError(Throwable cause) {
        System.out.println("Close: statusCode=" + cause.toString());
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println("Close: statusCode=" + statusCode + " " + reason);
    }

    @Override
    public void onWebSocketText(String message) {
        System.out.println("New data: " + message);
    }

    @Override
    public void onWebSocketConnect(Session session) {
        String sessionId = getSessionId(session.getUpgradeRequest().getCookies());
        game.onNewConnection(sessionId, session);

        try {
            session.getRemote().sendString("Hello!");
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (WebSocketException e2) {
            e2.printStackTrace();
        }
    }

    private static String getSessionId(List<HttpCookie> cookieList) {
        for(HttpCookie c : cookieList) {
            if (c.getName().equals("JSESSIONID")) {
                return c.getValue();
            }
        }
        return null;
    }

    public static class GameWebSocketCreator implements WebSocketCreator {
        private Game game;

        public GameWebSocketCreator(Game game) {
            this.game = game;
        }

        @Override
        public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) {
            return new GameWebSocketHandler(game);
        }
    }
}