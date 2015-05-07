package websocket.message;

import com.google.gson.JsonElement;
import model.Snake.Snake;

/**
 * egor, 18.04.15.
 */
public class SnakeUpdateMessage extends Message {
    private Snake snake;
    private int id;
    private JsonElement jsonElement = null;
    public SnakeUpdateMessage(Snake snake, int id){
        this.id = id;
        this.snake = snake;

    }

    public int getId() {
        return id;
    }

    public Snake getSnake() {
        return snake;
    }

    public JsonElement getJsonElement() {
        return jsonElement;
    }

    public void setJsonElement(JsonElement jsonElement) {
        this.jsonElement = jsonElement;
    }
}

