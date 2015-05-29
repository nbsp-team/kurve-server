package websocket;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import game.Room;
import model.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import service.Request;
import service.Response;
import service.ServiceManager;
import service.ServiceType;
import utils.Bundle;
import websocket.message.SnakePatchMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */

public class GameWebSocketHandler extends WebSocketAdapter implements Serializable {
    public static final Logger LOG = LogManager.getLogger(GameWebSocketHandler.class);
    private final ServiceManager serviceManager;

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
        CODE_START_ROUND_RESPONSE
    }

    private WebSocketMessageListener messageListener;
    private UserProfile userProfile;
    private Room room;
    private WebSocketConnection connection;

    public GameWebSocketHandler(ServiceManager serviceManager, UserProfile userProfile, WebSocketMessageListener messageListener) {
        this.userProfile = userProfile;
        this.messageListener = messageListener;
        this.serviceManager = serviceManager;
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        LOG.debug("WebSocket error: " + cause.toString() + " for user " + userProfile);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        LOG.debug("WebSocket closed: " + statusCode + " for user " + userProfile);
        messageListener.onDisconnect(this);

        Bundle bundle = new Bundle();
        bundle.putSerializable("handler", this);
        serviceManager.process(ServiceType.GAME_SERVICE, 0,
                new Request("on_disconnect", bundle));
    }

    @Override
    public void onWebSocketText(String message) {
        try {
            JsonObject jresponse = (JsonObject) new JsonParser().parse(message);
            MessageType responseType = MessageType.values()[jresponse.get("code").getAsInt()];

            Bundle bundle = new Bundle();
            bundle.putSerializable("handler", this);

            switch (responseType) {
                case CODE_ROOM_PLAYERS_RESPONSE:
                    break;
                case CODE_READY_REQUEST:
                    boolean isReady = jresponse.get("ready").getAsBoolean();
                    bundle.putBoolean("ready", isReady);
                    serviceManager.process(ServiceType.GAME_SERVICE, 0,
                            new Request("on_user_ready", bundle));
                    break;
                case CODE_READY_RESPONSE:
                    break;
                case CODE_INIT_STATE_RESPONSE:
                    break;
                case CODE_KEY_REQUEST:
                    boolean isLeft = jresponse.get("isLeft").getAsBoolean();
                    boolean isUp = jresponse.get("isUp").getAsBoolean();
                    bundle.putBoolean("left", isLeft);
                    bundle.putBoolean("up", isUp);
                    serviceManager.process(ServiceType.GAME_SERVICE, 0,
                            new Request("on_control", bundle));
                    break;
                case CODE_KEY_RESPONSE:
                    break;
                case CODE_SNAKE_UPDATES_RESPONSE:
                    break;
		        case CODE_SNAKE_PATCH_REQUEST:
                    JsonArray array = jresponse.get("ids").getAsJsonArray();
                    List<Integer> lostIds = new ArrayList<>();
                    for(JsonElement elem : array){
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
            }
        } catch (ClassCastException e) {
            // TODO: error response
            e.printStackTrace();
        }

    }

    @Override
    public void onWebSocketConnect(Session session) {
        connection = new WebSocketConnection(session);

        Bundle bundle = new Bundle();
        bundle.putSerializable("handler", this);
        Request request = new Request("on_new_connection", bundle, true);
        serviceManager.process(ServiceType.GAME_SERVICE, 0, request);
        Response response = serviceManager.waitResponse(request);
        room = (Room) response.getArgs().getSerializable("room");
    }

    public WebSocketConnection getConnection() {
        return connection;
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

        Room onNewConnection(GameWebSocketHandler handler);

        void onDisconnect(GameWebSocketHandler handler);

        void onUserReady(GameWebSocketHandler handler, boolean isReady);

        void onControl(GameWebSocketHandler handler, boolean isLeft, boolean isUp);

    }
}
