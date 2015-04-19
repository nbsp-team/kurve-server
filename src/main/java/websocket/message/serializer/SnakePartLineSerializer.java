package websocket.message.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Snake.SnakePartLine;

import java.lang.reflect.Type;

/**
 * Created by egor on 19.04.15.
 */
public class SnakePartLineSerializer implements JsonSerializer<SnakePartLine> {
    public JsonElement serialize(SnakePartLine src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        if(src.getX1() == 0) {
            System.out.println("WHY");
        }
        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("x1", src.getX1());
        jsonObject.addProperty("y1", src.getY1());
        jsonObject.addProperty("x2", src.getX2());
        jsonObject.addProperty("y2", src.getY2());
        jsonObject.addProperty("lineRadius", src.getlineRadius());

        return jsonObject;
    }
}