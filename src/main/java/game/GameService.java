package game;

import main.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import websocket.GameWebSocketHandler;
import websocket.message.ConnectedPlayerMessage;
import websocket.message.ControlMessage;
import websocket.message.RoomPlayersMessage;
import websocket.message.StartGameMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */
public class GameService implements GameWebSocketHandler.WebSocketMessageListener {
    public static final Logger LOG = LogManager.getLogger(GameService.class);

    private final int MIN_PLAYER_IN_ROOM;
    private final int MAX_PLAYER_IN_ROOM;

    private List<Room> rooms;

    public GameService() {
        MIN_PLAYER_IN_ROOM = Integer.valueOf(Main.mechanicsConfig.minPlayerNumber);
        MAX_PLAYER_IN_ROOM = Integer.valueOf(Main.mechanicsConfig.maxPlayerNumber);

        rooms = new ArrayList<>();
    }

    @Override
    public Room onNewConnection(GameWebSocketHandler handler) {
        LOG.debug("New WebSocket connection: " + handler.getUserProfile());

        if (handler.getUserProfile() == null) {
            handler.disconnect(GameWebSocketHandler.CLOSE_REASON_NO_AUTH, "Auth required");
            return null;
        }

        for (Room room : rooms) {
            if (room.getRoomState() == Room.RoomState.GAME) {
                continue;
            }

            Player player = room.getPlayerByUser(handler.getUserProfile());

            if (player != null) {
                player.addConnection(handler);
                player.sendMessage(new RoomPlayersMessage(room));
                room.broadcastMessageExceptUser(new ConnectedPlayerMessage(player,
                        room.getPlayerIdByUser(player.getUserProfile())), player.getUserProfile());
                return room;
            }

            if (room.getPlayerCount() < MAX_PLAYER_IN_ROOM) {
                connectUserToRoom(handler, room);
                return room;
            }
        }

        Room newRoom = new Room(this);
        connectUserToRoom(handler, newRoom);
        rooms.add(newRoom);
        return newRoom;
    }

    private void connectUserToRoom(GameWebSocketHandler handler, Room room) {
        String playerColor = getUnusedColor(room);
        Player newPlayer = new Player(playerColor, handler.getUserProfile());
        newPlayer.addConnection(handler);
        room.onNewPlayer(newPlayer);
        handler.setRoom(room);
    }

    private void checkRoomReady(Room room) {
        int readyCount = room.getReadyPlayerCount();

        if (readyCount >= MIN_PLAYER_IN_ROOM && readyCount <= MAX_PLAYER_IN_ROOM
                && readyCount == room.getPlayerCount() && room.getRoomState() == Room.RoomState.WAITING) {
            room.startGame();
        }
    }

    public void destroyRoom(Room room) {
        rooms.remove(room);
    }

    @Override
    public void onDisconnect(GameWebSocketHandler handler) {
        if (handler.getUserProfile() == null) {
            return;
        }

        Room userRoom = handler.getRoom();
        if (userRoom != null) {
            Player player = userRoom.getPlayerByUser(handler.getUserProfile());
            if (player.getConnectionCount() == 1) {
                userRoom.onPlayerDisconnect(player);
                handler.setRoom(null);
            } else {
                player.removeConnection(handler);
            }
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

                if (room.getRoomState() == Room.RoomState.GAME) {
                    handler.sendMessage(
                            new StartGameMessage(room, room.getPlayerIdByUser(player.getUserProfile()))
                    );
                }
            }
        }
    }

    @Override
    public void onControl(GameWebSocketHandler handler, boolean isLeft, boolean isUp) {
        Room room = handler.getRoom();
        if (room != null) {
            int sender = room.getPlayerIdByUser(handler.getUserProfile());
            room.broadcastMessageExceptConnection(
                    new ControlMessage(isLeft, isUp, sender),
                    handler
            );
            room.onKeyEvent(isLeft, isUp, sender);
        }
    }

    private String getUnusedColor(Room room) {
        for (String c : Player.playerColors) {
            if (!room.isColorUsed(c)) {
                return c;
            }
        }
        return "#000000";
    }
}