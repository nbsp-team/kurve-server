package websocket.message.serializer;

import com.google.gson.*;
import game.Player;
import websocket.GameWebSocketHandler;
import websocket.message.GameOverMessage;
import websocket.message.RatingUpdateMessage;

import java.lang.reflect.Type;

/**
 * Created by egor on 19.04.15.
 */
public class RatingUpdateMessageSerializer implements JsonSerializer<RatingUpdateMessage> {
    public JsonElement serialize(RatingUpdateMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_RATING_UPDATE_RESPONSE.ordinal());

        JsonArray playersArray = new JsonArray();
        for (Player player : src.getRoom().getPlayers()) {
            JsonObject playerObject = new JsonObject();

            playerObject.addProperty("user_id", player.getUserProfile().getId());
            playerObject.addProperty("first_name", player.getUserProfile().getFirstName());
            playerObject.addProperty("last_name", player.getUserProfile().getLastName());
            playerObject.addProperty("avatar", player.getUserProfile().getAvatarUrl());

            playerObject.addProperty("points", player.getPoints());
            playerObject.addProperty("color", player.getColor());
            playersArray.add(playerObject);
        }

        responseObject.add("results", playersArray);

        return responseObject;
    }
}