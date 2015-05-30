package websocket.message;

import game.Room;

/**
 * egor, 18.04.15.
 */
public class RatingUpdateMessage extends AbstractMessageToRoom {
    public RatingUpdateMessage(Room room) {
        super(room);
    }
}