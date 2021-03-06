package websocket;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import game.Room;
import model.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import websocket.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */

public class GameWebSocketHandler extends WebSocketAdapter {
    public static final Logger LOG = LogManager.getLogger(GameWebSocketHandler.class);
    public static final int CLOSE_REASON_NO_AUTH = 1;

    public enum MessageType {
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
        CODE_SNAKE_ARC_RESPONSE,
        CODE_SNAKE_PATCH_REQUEST,//15
        CODE_SNAKE_PATCH_RESPONSE,
        CODE_START_ROUND_RESPONSE,
        CODE_RATING_UPDATE_RESPONSE,
        CODE_CREATE_ROOM_REQUEST,
        CODE_CONNECT_TO_ROOM_REQUEST,
        CODE_GET_ROOMS_REQUEST,
        CODE_ROOMS_RESPONSE,
        CODE_CONNECTED_TO_ROOM_RESPONSE
    }

    private WebSocketMessageListener messageListener;
    private UserProfile userProfile;
    private Room room;
    private Session session;

    public GameWebSocketHandler(UserProfile userProfile, WebSocketMessageListener messageListener) {
        this.userProfile = userProfile;
        this.messageListener = messageListener;
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        System.out.println("[GameWebSocketHandler] WebSocket error: " + cause.toString() + " for user " + userProfile);
        messageListener.onDisconnect(this);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println("[GameWebSocketHandler] WebSocket closed: " + statusCode + " for user " + userProfile);
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
                case CODE_PLAYER_CONNECTED_RESPONSE:
                    break;
                case CODE_PLAYER_DISCONNECTED_RESPONSE:
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
                    boolean isLeft = jresponse.get("isLeft").getAsBoolean();
                    boolean isUp = jresponse.get("isUp").getAsBoolean();
                    messageListener.onControl(this, isLeft, isUp);

                    break;
                case CODE_KEY_RESPONSE:
                    break;
                case CODE_SNAKE_UPDATES_RESPONSE:
                    break;
                case CODE_ROOM_START_RESPONSE:
                    break;
                case CODE_SNAKE_ARC_RESPONSE:
                    break;
                case CODE_SNAKE_PATCH_REQUEST:
                    JsonArray array = jresponse.get("ids").getAsJsonArray();
                    List<Integer> lostIds = new ArrayList<>();
                    for (JsonElement elem : array) {
                        lostIds.add(elem.getAsInt());
                    }

                    room.sendPatchToUser(userProfile, lostIds);
                    break;
                case CODE_NEW_BONUS_RESPONSE:
                    break;
                case CODE_BONUS_EAT_RESPONSE:
                    break;
                case CODE_ROUND_END_RESPONSE:
                    break;
                case CODE_GAME_OVER_RESPONSE:
                    break;
                case CODE_SNAKE_PATCH_RESPONSE:
                    break;
                case CODE_START_ROUND_RESPONSE:
                    break;
                case CODE_RATING_UPDATE_RESPONSE:
                    break;
                case CODE_CREATE_ROOM_REQUEST:
                    break;
                case CODE_CONNECT_TO_ROOM_REQUEST:
                    messageListener.onConnectToRoom(
                            this,
                            jresponse.get("room").getAsString()
                    );
                    break;
                case CODE_GET_ROOMS_REQUEST:
                    messageListener.onGetRooms(
                            this
                    );
                    break;
            }
        } catch (ClassCastException e) {
            // TODO: error response
            e.printStackTrace();
        }

    }

    @Override
    public void onWebSocketConnect(Session session) {
        this.session = session;
        messageListener.onNewConnection(this);
    }

    public boolean sendMessage(Message message) {
        try {
            RemoteEndpoint remote = session.getRemote();

            synchronized (remote) {
                try {
                    remote.sendString(message.getBody());
                    return true;
                } catch (Exception we) {
                    System.out.println("[GameWebSocketHandler] Ошибка при отправке пакета (" + we.getMessage() + "). Дисконнектинг игрока.");
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("[GameWebSocketHandler] Ошибка при получении RemoteEndpoint (" + e.getMessage() + "). Дисконнектинг игрока.");
            return false;
        }
    }

    public void disconnect(int closeReason, String description) {
        session.close(closeReason, description);
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

    public interface WebSocketMessageListener {

        void onNewConnection(GameWebSocketHandler handler);

        void onDisconnect(GameWebSocketHandler handler);

        void onUserReady(GameWebSocketHandler handler, boolean isReady);

        void onControl(GameWebSocketHandler handler, boolean isLeft, boolean isUp);

        void onConnectToRoom(GameWebSocketHandler handler, String room);

        void onGetRooms(GameWebSocketHandler handler);
    }
}
