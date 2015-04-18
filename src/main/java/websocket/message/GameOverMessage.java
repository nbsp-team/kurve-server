package websocket.message;

import com.google.gson.*;
import game.Player;
import game.Room;
import websocket.GameWebSocketHandler;

import java.lang.reflect.Type;

/**
 * egor, 18.04.15.
 */
public class GameOverMessage extends Message {

    private Room room;


    public GameOverMessage(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }


    public static class serializer implements JsonSerializer<GameOverMessage> {
        public JsonElement serialize(GameOverMessage src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("code", GameWebSocketHandler.MessageType.CODE_GAME_OVER_RESPONSE.ordinal());

            JsonArray playersArray = new JsonArray();
            for(int i = 0; i < src.getRoom().getPlayerCount(); ++i) {
                Player player = src.getRoom().getPlayers().get(i);
                JsonObject playerObject = new JsonObject();
                playerObject.addProperty("username", player.getUserProfile().getLogin());
                playerObject.addProperty("points", player.getPoints());
                playerObject.addProperty("color", "#" + Integer.toHexString(player.getColor().getRGB()).substring(2));
                playersArray.add(playerObject);
            }

            responseObject.add("results", playersArray);

            return responseObject;
        }
    }
}
