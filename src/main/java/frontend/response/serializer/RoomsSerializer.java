package frontend.response.serializer;

import com.google.gson.*;
import frontend.response.RatingResponse;
import frontend.response.RoomsResponse;
import game.Room;
import model.UserProfile;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class RoomsSerializer implements JsonSerializer<RoomsResponse> {
    public JsonElement serialize(RoomsResponse src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("error", JsonNull.INSTANCE);

        JsonArray usersArray = new JsonArray();
        for (Room room : src.getRooms()) {
            if (room.isPrivate()) {
                continue;
            }

            usersArray.add(context.serialize(room));
        }

        JsonObject response = new JsonObject();
        response.add("rooms", usersArray);

        jsonObject.add("response", response);

        return jsonObject;
    }
}
