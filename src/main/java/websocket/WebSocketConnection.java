package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.message.Message;

import java.io.IOException;

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

    public void sendMessage(Message message) {
        try {
            webSocketSession.getRemote().sendString(
                    message.getBody()
            );

        } catch (IOException e) {
            // TODO: error response
            try {
                webSocketSession.getRemote().sendString(
                        "500"
                );
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void disconnect(int closeReason, String description) {
        webSocketSession.close(closeReason, description);
    }
}
