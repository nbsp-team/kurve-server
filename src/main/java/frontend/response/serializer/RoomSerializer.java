package frontend.response.serializer;

import com.google.gson.*;
import game.Player;
import game.Room;
import model.UserProfile;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class RoomSerializer implements JsonSerializer<Room> {
    public JsonElement serialize(Room src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("room_id", src.getId());
        jsonObject.addProperty("creation_date", src.getCreationDate());
        jsonObject.addProperty("capacity", src.getCapacity());
        jsonObject.add("owner", context.serialize(src.getOwner()));

        JsonArray playersArray = new JsonArray();
        for (Player player : src.getPlayers()) {
            JsonElement playerObject = context.serialize(player);
            playersArray.add(playerObject);
        }
        jsonObject.add("players", playersArray);

        return jsonObject;
    }
}