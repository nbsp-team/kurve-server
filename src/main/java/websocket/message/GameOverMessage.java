package websocket.message;

import game.Room;

/**
 * egor, 18.04.15.
 */
public class GameOverMessage extends Message {

    private Room room;


    public GameOverMessage(Room room) {
        this.room = room;
        buildBody();
    }

    public Room getRoom() {
        return room;
    }



}
