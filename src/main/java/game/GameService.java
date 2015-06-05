package game;

import dao.ScoresDao;
import main.Main;
import model.Score;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import websocket.GameWebSocketHandler;
import websocket.message.ConnectedPlayerMessage;
import websocket.message.ControlMessage;
import websocket.message.RoomPlayersMessage;
import websocket.message.StartGameMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */
public class GameService implements GameWebSocketHandler.WebSocketMessageListener {
    public static final Logger LOG = LogManager.getLogger(GameService.class);

    private final int MIN_PLAYER_IN_ROOM;
    private final int MAX_PLAYER_IN_ROOM;

    private final ScoresDao scoresDao;
    private List<Room> rooms;

    public GameService(ScoresDao scoresDao) {
        MIN_PLAYER_IN_ROOM = Integer.valueOf(Main.mechanicsConfig.minPlayerNumber);
        MAX_PLAYER_IN_ROOM = Integer.valueOf(Main.mechanicsConfig.maxPlayerNumber);

        rooms = new ArrayList<>();
        this.scoresDao = scoresDao;
    }

    @Override
    public Room onNewConnection(GameWebSocketHandler handler) {

        System.out.println("New WebSocket connection: " + handler.getUserProfile());
        System.out.println("Rooms: " + rooms.size());

        if (handler.getUserProfile() == null) {
            System.out.println("Disconnect: auth required");
            handler.disconnect(GameWebSocketHandler.CLOSE_REASON_NO_AUTH , "Auth required");
            return null;
        }

        for (Room room : rooms) {
            if (room.getRoomState() == Room.RoomState.GAME) {
                continue;
            }

            Player player = room.getPlayerByUser(handler.getUserProfile());

            if (player != null) {
                System.out.println("User already in room, attaching: " + handler.getUserProfile().getFirstName());

                player.addConnection(handler);
                player.sendMessage(new RoomPlayersMessage(room));
                room.broadcastMessageExceptUser(new ConnectedPlayerMessage(player,
                        room.getPlayerIdByUser(player.getUserProfile())), player.getUserProfile());
                return room;
            }

            if (room.getPlayerCount() < MAX_PLAYER_IN_ROOM) {
                System.out.println("Founded room, connecting: " + handler.getUserProfile().getFirstName());

                connectUserToRoom(handler, room);
                return room;
            }
        }

        System.out.println("Creating new room: " + handler.getUserProfile().getFirstName());
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
        System.out.println("Destroying room: " + room);
        rooms.remove(room);
    }

    @Override
    public void onDisconnect(GameWebSocketHandler handler) {
        if (handler.getUserProfile() == null) {
            System.out.println("User disconnected: unknown profile");
            return;
        }

        System.out.println("User disconnected: " + handler.getUserProfile().getFirstName());

        Room userRoom = handler.getRoom();
        if (userRoom != null) {
            Player player = userRoom.getPlayerByUser(handler.getUserProfile());
            if (player.getConnectionCount() == 1) {
                System.out.println("Removed player (connection count = 1) " + handler.getUserProfile().getFirstName());
                userRoom.onPlayerDisconnect(player);
                handler.setRoom(null);
            } else {
                System.out.println("Removed connection (connection count != 1) " + handler.getUserProfile().getFirstName());
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
                            new StartGameMessage(room, room.getPlayerIdByUser(player.getUserProfile()), room.getCurrentRound(), Room.ROUND_NUMBER)
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

    public void writePointsToDb(Room room) {
        room.getPlayers().stream().filter(player -> player.getPoints() > 0).forEach(player -> {
            scoresDao.insert(
                    new Score(player.getUserProfile().getId(), player.getPoints(), new Date())
            );
        });
    }

    private String getUnusedColor(Room room) {
        for (String c : Player.playerColors) {
            if (!room.isColorUsed(c)) {
                return c;
            }
        }
        return "#000000";
    }

    public long getRoomCount() {
        return rooms.size();
    }

    public List<Room> getRooms() {
        return rooms;
    }
}