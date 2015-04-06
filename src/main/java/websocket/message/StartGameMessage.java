package websocket.message;

import com.google.gson.*;
import game.Player;
import game.Room;
import websocket.GameWebSocketHandler;

import java.lang.reflect.Type;

/**
 * nickolay, 17.03.15.
 */
public class StartGameMessage extends Message {
    private Room room;

    public StartGameMessage(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public static class serializer implements JsonSerializer<StartGameMessage> {
        public JsonElement serialize(StartGameMessage src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_INIT_STATE_RESPONSE.ordinal());

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
