package websocket.message;

import com.google.gson.*;
import game.Player;
import game.Room;
import websocket.GameWebSocketHandler;

import java.lang.reflect.Type;

/**
 * nickolay, 17.03.15.
 */
public class RoomPlayersMessage extends Message {
    private Room room;

    public RoomPlayersMessage(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
