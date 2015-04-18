package websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import game.Room;
import interfaces.AccountService;
import main.MemoryAccountService;
import model.UserProfile;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import utils.SessionManager;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * nickolay, 21.02.15.
 */

public class GameWebSocketHandler extends WebSocketAdapter {
    public static enum MessageType {
        CODE_ROOM_PLAYERS_RESPONSE,
        CODE_PLAYER_CONNECTED_RESPONSE,
        CODE_PLAYER_DISCONNECTED_RESPONSE,
        CODE_READY_REQUEST,
        CODE_READY_RESPONSE,
        CODE_INIT_STATE_RESPONSE,
        CODE_KEY_REQUEST,
        CODE_KEY_RESPONSE,
        CODE_SNAKE_UPDATES_RESPONSE,
        CODE_NEW_BONUS_RESPONSE,
        CODE_BONUS_EAT_RESPONSE,
        CODE_ROUND_END_RESPONSE,
        CODE_GAME_OVER_RESPONSE,
        CODE_ROOM_START_RESPONSE,
        CODE_SNAKE_ARC_RESPONSE
    }

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
        try {
            JsonObject jresponse = (JsonObject) new JsonParser().parse(message);
            MessageType responseType = MessageType.values()[jresponse.get("code").getAsInt()];

            switch (responseType) {
                case CODE_ROOM_PLAYERS_RESPONSE:
                    break;
                case CODE_READY_REQUEST:
                    boolean isReady = jresponse.get("ready").getAsBoolean();
                    messageListener.onUserReady(userProfile, isReady);
                    break;
                case CODE_READY_RESPONSE:
                    break;
                case CODE_INIT_STATE_RESPONSE:
                    break;
                case CODE_KEY_REQUEST:
                    System.out.append("Event key: ").append(message).append('\n');
                    boolean isLeft = jresponse.get("isLeft").getAsBoolean();
                    boolean isUp = jresponse.get("isUp").getAsBoolean();
                    room.onKeyEvent(isLeft, isUp, userProfile);
                    
                    break;
                case CODE_KEY_RESPONSE:
                    break;
                case CODE_SNAKE_UPDATES_RESPONSE:
                    break;
                case CODE_NEW_BONUS_RESPONSE:
                    break;
                case CODE_BONUS_EAT_RESPONSE:
                    break;
                case CODE_ROUND_END_RESPONSE:
                    break;
                case CODE_GAME_OVER_RESPONSE:
                    break;
            }
        } catch (ClassCastException e) {
            // TODO: error response
            e.printStackTrace();
        }

    }

    private Room room;

    @Override
    public void onWebSocketConnect(Session session) {
        room = messageListener.onNewConnection(userProfile, new WebSocketConnection(session));
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
        public Room onNewConnection(UserProfile user, WebSocketConnection connection);
        public void onDisconnect(UserProfile user);
        public void onUserReady(UserProfile user, boolean isReady);
    }
}