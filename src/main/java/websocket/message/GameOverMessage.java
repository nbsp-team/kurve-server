package websocket.message;

import game.Room;

/**
 * egor, 18.04.15.
 */
public class GameOverMessage extends AbstractMessageToRoom {

    public GameOverMessage(Room room) {
        super(room);
    }
}
