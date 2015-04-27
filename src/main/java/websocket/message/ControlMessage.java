package websocket.message;

/**
 * egor, 18.04.15.
 */
public class ControlMessage extends Message {
    private boolean isLeft, isUp;
    private int playerId;

    public ControlMessage(boolean isLeft, boolean isUp, int playerId) {
        this.isLeft = isLeft;
        this.isUp = isUp;
        this.playerId = playerId;
    }

    public boolean getIsLeft() {
        return isLeft;
    }

    public boolean getIsUp() {
        return isUp;
    }

    public int getPlayerId() {
        return playerId;
    }


}
