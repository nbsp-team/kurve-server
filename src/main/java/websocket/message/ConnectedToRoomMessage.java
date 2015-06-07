package websocket.message;

import game.Player;

/**
 * Created by Dimorinny on 20.03.15.
 */
public class ConnectedToRoomMessage extends Message {

    private String roomId;

    public ConnectedToRoomMessage(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}
