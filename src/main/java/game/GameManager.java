package game;

import main.Main;
import model.UserProfile;
import websocket.GameWebSocketHandler;
import websocket.WebSocketConnection;
import websocket.message.ControlMessage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */
public class GameManager implements GameWebSocketHandler.WebSocketMessageListener {
    private final int MIN_PLAYER_IN_ROOM;
    private final int MAX_PLAYER_IN_ROOM;

    private List<Room> rooms;

    public GameManager() {
        MIN_PLAYER_IN_ROOM = Integer.valueOf(Main.mechanicsConfig.minPlayerNumber);
        MAX_PLAYER_IN_ROOM = Integer.valueOf(Main.mechanicsConfig.maxPlayerNumber);

        System.out.println(MAX_PLAYER_IN_ROOM);

        rooms = new ArrayList<>();
    }

    @Override
    public void onNewConnection(GameWebSocketHandler handler, WebSocketConnection connection) {
        System.out.println("New ws: " + handler.getUserProfile());

        if (handler.getUserProfile() == null) {
            // connection.close();
            return;
        }

        for (Room room : rooms) {
            Player player = room.getPlayerByUser(handler.getUserProfile());

            if (player != null) {
                player.addConnection(connection);
                return;
            }

            if (room.getPlayerCount() < MAX_PLAYER_IN_ROOM) {
                connectUserToRoom(connection, handler, room);
                return;
            }
        }

        Room newRoom = new Room();
        connectUserToRoom(connection, handler, newRoom);
        rooms.add(newRoom);
    }

    private void connectUserToRoom(WebSocketConnection connection, GameWebSocketHandler handler, Room room) {
        Color playerColor = getUnusedColor(room);
        Player newPlayer = new Player(playerColor, handler.getUserProfile());
        newPlayer.addConnection(connection);
        room.onNewPlayer(newPlayer);
        handler.setRoom(room);
    }

    private void checkRoomReady(Room room) {
        int readyCount = room.getReadyPlayerCount();
        System.out.println("Ready: " + readyCount);
        if (readyCount >= MIN_PLAYER_IN_ROOM && readyCount <= MAX_PLAYER_IN_ROOM) {
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
    public void onControl(GameWebSocketHandler handler, ControlMessage.KeyCode key, boolean pressed) {
        Room room = handler.getRoom();
        if (room != null) {
            Player player = room.getPlayerByUser(handler.getUserProfile());
            room.broadcastMessageExceptUser(
                    new ControlMessage(player, key, pressed),
                    handler.getUserProfile()
            );
        }
    }

    private Color getUnusedColor(Room room) {
        for(Color c : Player.playerColors) {
            if (!room.isColorUsed(c)) {
                return c;
            }
        }
        return Color.BLACK;
    }
}