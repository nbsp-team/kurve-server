package websocket.message;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import game.Player;
import websocket.GameWebSocketHandler;

import java.lang.reflect.Type;

/**
 * nickolay, 20.03.15.
 */
public class ControlMessage extends Message {
    public enum KeyCode {
        KEY_CODE_LEFT,
        KEY_CODE_RIGHT,
        KEY_CODE_BONUS
    }

    private KeyCode keyCode;
    private boolean isPressed;
    private Player player;

    public ControlMessage(Player player, KeyCode keyCode, boolean isPressed) {
        this.player = player;
        this.isPressed = isPressed;
        this.keyCode = keyCode;
    }

    public Player getPlayer() {
        return player;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public class serializer implements JsonSerializer<ControlMessage> {
        public JsonElement serialize(ControlMessage src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_KEY_RESPONSE.ordinal());

            responseObject.addProperty("player_id", src.getPlayer().getId());
            responseObject.addProperty("keycode", src.getKeyCode().ordinal());
            responseObject.addProperty("press", src.isPressed());

            return responseObject;
        }
    }
}
