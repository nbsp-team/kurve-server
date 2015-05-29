package websocket.message;

import game.Room;

/**
 * Created by egor on 30.05.15.
 */
public class AbstractMessageToRoom extends Message {
    private Room room;


    public AbstractMessageToRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
