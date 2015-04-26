package websocket.message.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Bonus.Bonus;

import java.lang.reflect.Type;

/**
 * Created by egor on 23.04.15.
 */
public class BonusSerializer implements JsonSerializer<Bonus> {
    public JsonElement serialize(Bonus src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("kind", src.getKind().ordinal());
        responseObject.addProperty("x", Math.floor(src.getX()));
        responseObject.addProperty("y", Math.floor(src.getY()));
        responseObject.addProperty("id", src.getId());
        responseObject.addProperty("color", src.getColor());

        return responseObject;
    }
}