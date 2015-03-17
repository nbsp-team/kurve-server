package websocket.message;

import com.google.gson.*;
import game.Player;
import game.Room;

import java.lang.reflect.Type;

/**
 * nickolay, 17.03.15.
 */
public class ReadyMessage extends Message {
    private Room room;

    public ReadyMessage(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public static class serializer implements JsonSerializer<ReadyMessage> {
        public JsonElement serialize(ReadyMessage src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("code", 0);

            JsonArray playersArray = new JsonArray();
            for(int i = 0; i < src.getRoom().getPlayerCount(); ++i) {
                Player player = src.getRoom().getPlayers().get(i);
                JsonElement playerObject = context.serialize(player);
                if (playerObject.isJsonObject()) {
                    ((JsonObject) playerObject).addProperty("player_id", i);
                }
                playersArray.add(playerObject);
            }

            responseObject.add("players", playersArray);

            return responseObject;
        }
    }
}
