package websocket.message.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Snake.SnakePartArc;

import java.lang.reflect.Type;

/**
 * Created by egor on 19.04.15.
 */
public class SnakePartArcSerializer implements JsonSerializer<SnakePartArc> {
    public JsonElement serialize(SnakePartArc src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("x", Math.round(src.getX()));
        jsonObject.addProperty("y", Math.round(src.getY()));
        jsonObject.addProperty("angle", src.getAngle());
        jsonObject.addProperty("angle2", src.getAngle2());
        jsonObject.addProperty("radius", Math.round(src.getRadius()));
        jsonObject.addProperty("lineRadius", src.getLineRadius());
        jsonObject.addProperty("clockwise", src.isClockwise());
        return jsonObject;
    }
}