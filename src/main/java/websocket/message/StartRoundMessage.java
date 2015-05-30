package websocket.message;

import game.Room;

/**
 * egor, 18.04.15.
 */
public class StartRoundMessage extends AbstractStartMessage {
    private int roundNumber;

    public StartRoundMessage(Room room, int playerId, int roundNumber) {
        super(room, playerId);
        this.roundNumber = roundNumber;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
}
