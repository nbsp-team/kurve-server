package websocket.message.serializer;

import com.google.gson.*;
import game.Player;
import main.Main;
import model.Snake.Snake;
import websocket.GameWebSocketHandler;
import websocket.message.StartRoundMessage;

import java.lang.reflect.Type;

/**
 * nickolay, 18.04.15.
 */
public class StartRoundMessageSerializer implements JsonSerializer<StartRoundMessage> {
    public JsonElement serialize(StartRoundMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_START_ROUND_RESPONSE.ordinal());
        responseObject.addProperty("FPS", Integer.valueOf(Main.mechanicsConfig.FPS));
        responseObject.addProperty("width", Integer.valueOf(Main.mechanicsConfig.gameFieldWidth));
        responseObject.addProperty("height", Integer.valueOf(Main.mechanicsConfig.gameFieldHeight));
        responseObject.addProperty("speed", Snake.defaultSpeed);
        responseObject.addProperty("angleSpeed", 180 / (2 * Math.PI) * (double) Snake.defaultSpeed / Snake.defaultTurnRadius);
        responseObject.addProperty("holeLength", Snake.holeLength);
        responseObject.addProperty("myId", src.getPlayerId());
        responseObject.addProperty("roundNumber", src.getRoundNumber());
        responseObject.addProperty("countdown", Integer.valueOf(Main.mechanicsConfig.gameStartCountdown));
        JsonArray playersArray = new JsonArray();
        for (int i = 0; i < src.getRoom().getPlayerCount(); ++i) {
            Player player = src.getRoom().getPlayers().get(i);
            JsonElement playerObject = context.serialize(player);
            playersArray.add(playerObject);
        }

        responseObject.add("players", playersArray);

        return responseObject;
    }
}