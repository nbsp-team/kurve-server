package websocket.message.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import utils.MathUtils;
import model.Snake.SnakePartLine;

import java.lang.reflect.Type;

/**
 * Created by egor on 19.04.15.
 */
public class SnakePartLineSerializer implements JsonSerializer<SnakePartLine> {
    public JsonElement serialize(SnakePartLine src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("x1", MathUtils.shortDouble(src.getX1()));
        jsonObject.addProperty("y1", MathUtils.shortDouble(src.getY1()));
        jsonObject.addProperty("x2", MathUtils.shortDouble(src.getX2()));
        jsonObject.addProperty("y2", MathUtils.shortDouble(src.getY2()));
        jsonObject.addProperty("lineRadius", src.getlineRadius());

        return jsonObject;
    }
}