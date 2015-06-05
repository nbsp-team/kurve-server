package websocket.message.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import game.Player;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class PlayerSerializer implements JsonSerializer<Player> {
    public JsonElement serialize(Player src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("user_id", src.getUserProfile().getId());
        jsonObject.addProperty("first_name", src.getUserProfile().getFirstName());
        jsonObject.addProperty("last_name", src.getUserProfile().getLastName());
        jsonObject.addProperty("avatar", src.getUserProfile().getAvatarUrl());

        jsonObject.addProperty("player_id", src.getId());
        jsonObject.addProperty("global_rating", src.getUserProfile().getGlobalRating());
        jsonObject.addProperty("is_ready", src.isReady());
        jsonObject.addProperty("color",
                src.getColor()
        );

        return jsonObject;
    }
}