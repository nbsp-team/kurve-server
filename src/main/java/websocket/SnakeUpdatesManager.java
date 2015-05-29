package websocket;

import com.google.gson.JsonElement;
import game.Room;
import model.Snake.Snake;
import websocket.message.SnakeUpdateMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egor on 06.05.15.
 */
public class SnakeUpdatesManager {
    private int id = 0;
    private Room room;
    List<SnakeUpdateMessage> messages;

    public SnakeUpdatesManager(Room room){
        this.room = room;
        messages = new ArrayList<>();
    }

    public synchronized void broadcast(Snake snake){
        SnakeUpdateMessage msg = new SnakeUpdateMessage(snake, id);
        messages.add(id, msg);
        id++;
        room.broadcastMessage(msg);
    }

    public List<SnakeUpdateMessage> getListByIds(List<Integer> ids){
        List<SnakeUpdateMessage> list = new ArrayList<>();
        for(Integer i : ids){
            list.add(messages.get(i));
        }
        return list;
    }
}
