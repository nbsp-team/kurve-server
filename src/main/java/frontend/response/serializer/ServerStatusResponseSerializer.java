package frontend.response.serializer;

import com.google.gson.*;
import frontend.response.ServerStatusResponse;
import game.Player;
import game.Room;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */

public class ServerStatusResponseSerializer implements JsonSerializer<ServerStatusResponse> {
    public JsonElement serialize(ServerStatusResponse src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("error", JsonNull.INSTANCE);

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("userCount", src.getUserCount());
        responseObject.addProperty("sessionCount", src.getSessionCount());
        responseObject.addProperty("roomCount", src.getRoomCount());

        JsonArray rooms = new JsonArray();
        for(Room room : src.getGameService().getRooms()) {
            JsonObject roomInfo = new JsonObject();

            roomInfo.addProperty("creationDate", room.getCreationDate());

            JsonArray players = new JsonArray();
            for(Player player : room.getPlayers()) {
                players.add(context.serialize(player));
            }

            roomInfo.add("players", players);

            rooms.add(roomInfo);
        }
        responseObject.add("rooms", rooms);

        jsonObject.add("response", responseObject);
        return jsonObject;
    }
}