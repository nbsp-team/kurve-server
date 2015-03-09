package websocket;

import org.eclipse.jetty.websocket.api.Session;

/**
 * nickolay, 09.03.15.
 */
public class WebSocketConnection {
    public static final int CLOSE_REASON_GAMEOVER = 0;
    public static final int CLOSE_REASON_NO_AUTH = 1;
    public static final int CLOSE_REASON_ROOM_NOT_FOUND = 2;

    private Session webSocketSession;

    public WebSocketConnection(Session webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public Session getSession() {
        return webSocketSession;
    }

    public void disconnect(int closeReason, String description) {
        webSocketSession.close(closeReason, description);
    }
}
