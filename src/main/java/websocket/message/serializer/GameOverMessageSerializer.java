package websocket.message.serializer;

import com.google.gson.*;
import game.Player;
import websocket.GameWebSocketHandler;
import websocket.message.GameOverMessage;

import java.lang.reflect.Type;

/**
 * Created by egor on 19.04.15.
 */
public class GameOverMessageSerializer implements JsonSerializer<GameOverMessage> {
    public JsonElement serialize(GameOverMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_GAME_OVER_RESPONSE.ordinal());

        JsonArray playersArray = new JsonArray();
        for(int i = 0; i < src.getRoom().getPlayerCount(); ++i) {
            Player player = src.getRoom().getPlayers().get(i);
            JsonObject playerObject = new JsonObject();
            playerObject.addProperty("username", player.getUserProfile().getLogin());
            playerObject.addProperty("points", player.getPoints());
            playerObject.addProperty("color", player.getColor());
            playersArray.add(playerObject);
        }

        responseObject.add("results", playersArray);

        return responseObject;
    }
}