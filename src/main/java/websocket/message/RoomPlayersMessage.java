package websocket.message;

import com.google.gson.*;
import game.Player;
import game.Room;
import websocket.GameWebSocketHandler;

import java.lang.reflect.Type;
import java.util.List;

/**
 * nickolay, 17.03.15.
 */
public class RoomPlayersMessage extends Message {
    private Room room;

    public RoomPlayersMessage(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public static class serializer implements JsonSerializer<RoomPlayersMessage> {
        public JsonElement serialize(RoomPlayersMessage src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_ROOM_PLAYERS_RESPONSE.ordinal());

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
