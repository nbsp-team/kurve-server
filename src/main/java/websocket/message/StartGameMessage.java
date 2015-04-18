package websocket.message;

import com.google.gson.*;
import game.GameField;
import game.Player;
import game.Room;
import model.Snake.Snake;
import websocket.GameWebSocketHandler;

import java.lang.reflect.Type;

/**
 * egor, 18.04.15.
 */
public class StartGameMessage extends Message {
    private Room room;
    private int playerId;

    public StartGameMessage(Room room, int playerId) {
        this.playerId = playerId;

        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public int getPlayerId() {
        return playerId;
    }


}
