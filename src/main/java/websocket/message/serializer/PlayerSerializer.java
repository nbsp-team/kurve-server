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

        jsonObject.addProperty("username", src.getUserProfile().getLogin());
        jsonObject.addProperty("player_id", src.getId());
        jsonObject.addProperty("global_rating", 0);
        jsonObject.addProperty("is_ready", src.isReady());
        jsonObject.addProperty("color",
                "#" + Integer.toHexString(src.getColor().getRGB()).substring(2)
        );

        return jsonObject;
    }
}