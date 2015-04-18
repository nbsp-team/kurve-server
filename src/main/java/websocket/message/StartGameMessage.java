package websocket.message;

import com.google.gson.*;
import game.GameField;
import game.Player;
import game.Room;
import model.Snake.Snake;
import websocket.GameWebSocketHandler;

import java.lang.reflect.Type;

/**
 * egor, 18.04.15.
 */
public class StartGameMessage extends Message {
    private Room room;
    private int playerId;

    public StartGameMessage(Room room, int playerId) {
        this.playerId = playerId;
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public int getPlayerId() {
        return playerId;
    }

    public static class serializer implements JsonSerializer<StartGameMessage> {
        public JsonElement serialize(StartGameMessage src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_ROOM_START_RESPONSE.ordinal());
            responseObject.addProperty("FPS", GameField.FPS);
            responseObject.addProperty("width", GameField.width);
            responseObject.addProperty("height", GameField.height);
            responseObject.addProperty("speed", Snake.defaultSpeed);
            responseObject.addProperty("angleSpeed", Snake.defaultAngleSpeed);
            responseObject.addProperty("partLength", Snake.defaultPartLength);
            responseObject.addProperty("holeLength", Snake.defaultHoleLength);
            responseObject.addProperty("myId", src.getPlayerId());
            JsonArray playersArray = new JsonArray();
            for(int i = 0; i < src.getRoom().getPlayerCount(); ++i) {
                Player player = src.getRoom().getPlayers().get(i);
                JsonElement playerObject = context.serialize(player);
                playersArray.add(playerObject);
            }

            responseObject.add("players", playersArray);

            return responseObject;
        }
    }
}
