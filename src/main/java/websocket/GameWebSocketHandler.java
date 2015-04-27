package websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import game.Room;
import model.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

/**
 * nickolay, 21.02.15.
 */

public class GameWebSocketHandler extends WebSocketAdapter {
    public static final Logger LOG = LogManager.getLogger(GameWebSocketHandler.class);

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
        LOG.debug("WebSocket error: " + cause.toString() + " for user " + userProfile);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        LOG.debug("WebSocket closed: " + statusCode + " for user " + userProfile);
        messageListener.onDisconnect(this);
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
                    messageListener.onUserReady(this, isReady);
                    break;
                case CODE_READY_RESPONSE:
                    break;
                case CODE_INIT_STATE_RESPONSE:
                    break;
                case CODE_KEY_REQUEST:
                    //System.out.append("Event key: ").append(message).append('\n');
                    boolean isLeft = jresponse.get("isLeft").getAsBoolean();
                    boolean isUp = jresponse.get("isUp").getAsBoolean();
                    messageListener.onControl(this, isLeft, isUp);

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

        room = messageListener.onNewConnection(this, new WebSocketConnection(session));
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public static interface WebSocketMessageListener {

        public Room onNewConnection(GameWebSocketHandler handler, WebSocketConnection connection);

        public void onDisconnect(GameWebSocketHandler handler);

        public void onUserReady(GameWebSocketHandler handler, boolean isReady);

        public void onControl(GameWebSocketHandler handler, boolean isLeft, boolean isUp);

    }
}