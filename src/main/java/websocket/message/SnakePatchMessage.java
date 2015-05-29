package websocket.message;


import java.util.List;

/**
 * Created by egor on 06.05.15.
 */
public class SnakePatchMessage extends Message {
    private List<SnakeUpdateMessage> updates;

    public SnakePatchMessage(List<SnakeUpdateMessage> updates) {
        this.updates = updates;
    }

    public List<SnakeUpdateMessage> getUpdates() {
        return updates;
    }
}
