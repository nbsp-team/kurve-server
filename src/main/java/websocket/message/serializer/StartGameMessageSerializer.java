package websocket.message.serializer;

import com.google.gson.*;
import game.Player;
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

        JsonArray playersArray = new JsonArray();
        for(int i = 0; i < src.getRoom().getPlayerCount(); ++i) {
            Player player = src.getRoom().getPlayers().get(i);
            JsonElement playerObject = context.serialize(player);
            playersArray.add(playerObject);
        }

        responseObject.add("players", playersArray);

        return responseObject;
    }
}