package websocket.message;

import game.Room;

/**
 * egor, 18.04.15.
 */
public class StartRoundMessage extends AbstractStartMessage {
    private final int roundCount;
    private final int currentRound;

    public StartRoundMessage(Room room, int playerId, int currentRound, int roundCount) {
        super(room, playerId);
        this.currentRound = currentRound;
        this.roundCount = roundCount;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getRoundCount() {
        return roundCount;
    }
}
