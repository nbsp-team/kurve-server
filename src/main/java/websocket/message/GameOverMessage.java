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



}
