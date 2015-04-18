package websocket.message;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Snake.Snake;
import websocket.GameWebSocketHandler;

import java.lang.reflect.Type;

/**
 * egor, 18.04.15.
 */
public class SnakeUpdateMessage extends Message {
    private Snake snake;
    public SnakeUpdateMessage(Snake snake){

        this.snake = snake;
    }

    public Snake getSnake() {
        return snake;
    }

    public static class serializer implements JsonSerializer<SnakeUpdateMessage> {
        public JsonElement serialize(SnakeUpdateMessage src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_SNAKE_ARC_RESPONSE.ordinal());

            responseObject.add("snake", src.getSnake().getUpdatesJson(context));

            return responseObject;
        }
    }
}
