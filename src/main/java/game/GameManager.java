package game;

import main.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import websocket.GameWebSocketHandler;
import websocket.WebSocketConnection;
import websocket.message.ControlMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */
public class GameManager implements GameWebSocketHandler.WebSocketMessageListener {
    public static final Logger LOG = LogManager.getLogger(GameManager.class);

    private final int MIN_PLAYER_IN_ROOM;
    private final int MAX_PLAYER_IN_ROOM;

    private List<Room> rooms;

    public GameManager() {
        MIN_PLAYER_IN_ROOM = Integer.valueOf(Main.mechanicsConfig.minPlayerNumber);
        MAX_PLAYER_IN_ROOM = Integer.valueOf(Main.mechanicsConfig.maxPlayerNumber);

        rooms = new ArrayList<>();
    }

    @Override

    public Room onNewConnection(GameWebSocketHandler handler, WebSocketConnection connection) {
        LOG.debug("New WebSocket connection: " + handler.getUserProfile());

        if (handler.getUserProfile() == null) {
            // connection.close();
            return null;
        }

        for (Room room : rooms) {
            Player player = room.getPlayerByUser(handler.getUserProfile());

            if (player != null) {
                player.addConnection(connection);
                return room;
            }

            if (room.getPlayerCount() < MAX_PLAYER_IN_ROOM) {
                connectUserToRoom(connection, handler, room);
                return room;
            }
        }

        Room newRoom = new Room();
        connectUserToRoom(connection, handler, newRoom);
        rooms.add(newRoom);
        return newRoom;
    }

    private void connectUserToRoom(WebSocketConnection connection, GameWebSocketHandler handler, Room room) {
        String playerColor = getUnusedColor(room);
        Player newPlayer = new Player(playerColor, handler.getUserProfile());
        newPlayer.addConnection(connection);
        room.onNewPlayer(newPlayer);
        handler.setRoom(room);
    }

    private void checkRoomReady(Room room) {
        int readyCount = room.getReadyPlayerCount();

        if (readyCount >= MIN_PLAYER_IN_ROOM && readyCount <= MAX_PLAYER_IN_ROOM
                && readyCount == room.getPlayerCount()) {
            room.startGame();
        }
    }

    @Override
    public void onDisconnect(GameWebSocketHandler handler) {
        if (handler.getUserProfile() == null) {
            return;
        }

        Room userRoom = handler.getRoom();
        if (userRoom != null) {
            Player player = userRoom.getPlayerByUser(handler.getUserProfile());
            userRoom.onPlayerDisconnect(player);
            handler.setRoom(null);
        }
    }

    @Override
    public void onUserReady(GameWebSocketHandler handler, boolean isReady) {
        if (handler.getUserProfile() != null) {
            Room room = handler.getRoom();
            if (room != null) {
                Player player = room.getPlayerByUser(handler.getUserProfile());
                room.onPlayerReady(player, isReady);
                checkRoomReady(room);
            }
        }
    }

    @Override
    public void onControl(GameWebSocketHandler handler, boolean isLeft, boolean isUp) {
        Room room = handler.getRoom();
        if (room != null) {
            int sender = room.getPlayerIdByUser(handler.getUserProfile());
            room.broadcastMessageExceptUser(
                    new ControlMessage(isLeft, isUp, sender),
                    handler.getUserProfile()
            );
            room.onKeyEvent(isLeft, isUp, sender);
        }
    }

    private String getUnusedColor(Room room) {
        for(String c : Player.playerColors) {
            if (!room.isColorUsed(c)) {
                return c;
            }
        }
        return "#000000";
    }
}