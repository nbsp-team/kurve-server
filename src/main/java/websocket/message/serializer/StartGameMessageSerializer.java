package websocket.message.serializer;

import com.google.gson.*;
import game.Player;
import main.Main;
import model.Snake.Snake;
import websocket.GameWebSocketHandler;
import websocket.message.StartGameMessage;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class StartGameMessageSerializer implements JsonSerializer<StartGameMessage> {
    public JsonElement serialize(StartGameMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_INIT_STATE_RESPONSE.ordinal());
        responseObject.addProperty("FPS",Main.mechanicsConfig.getInt("FPS"));
        responseObject.addProperty("width", Main.mechanicsConfig.getInt("gameField.width"));
        responseObject.addProperty("height", Main.mechanicsConfig.getInt("gameField.height"));
        responseObject.addProperty("speed", Snake.defaultSpeed);
        responseObject.addProperty("angleSpeed", 180 / (2 * Math.PI) * (double) Snake.defaultSpeed / Snake.defaultTurnRadius);
        responseObject.addProperty("holeLength", Snake.holeLength);
        responseObject.addProperty("myId", src.getPlayerId());
        responseObject.addProperty("countdown", Main.mechanicsConfig.getInt("gameStartCountdown"));
        responseObject.addProperty("currentRound", src.getCurrentRound());
        responseObject.addProperty("roundNumber", src.getRoundCount());

        JsonArray playersArray = new JsonArray();
        for (Player player : src.getRoom().getPlayers()) {
            JsonElement playerObject = context.serialize(player);
            playersArray.add(playerObject);
        }

        responseObject.add("players", playersArray);

        return responseObject;
    }
}