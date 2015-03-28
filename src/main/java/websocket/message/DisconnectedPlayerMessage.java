package websocket.message;

import com.google.gson.*;
import game.Player;
import websocket.GameWebSocketHandler;

import java.lang.reflect.Type;

/**
 * Created by Dimorinny on 20.03.15.
 */
public class DisconnectedPlayerMessage extends Message {

    private Player player;
    private int playerId;

    public DisconnectedPlayerMessage(Player player, int playerId) {
        this.player = player;
        this.playerId = playerId;

    }

    public int getPlayerId() {
        return playerId;
    }

    public Player getPlayer() {
        return player;
    }

    public static class serializer implements JsonSerializer<DisconnectedPlayerMessage> {
        public JsonElement serialize(DisconnectedPlayerMessage src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_PLAYER_DISCONNECTED_RESPONSE.ordinal());

            JsonObject playerObject = (JsonObject) context.serialize(src.getPlayer());
            playerObject.addProperty("player_id", src.getPlayerId());

            responseObject.add("player", playerObject);

            return responseObject;
        }
    }
}