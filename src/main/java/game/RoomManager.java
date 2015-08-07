package game;

import main.Main;
import model.UserProfile;
import utils.RandomUtils;
import websocket.GameWebSocketHandler;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * nickolay, 07.06.15.
 */
public class RoomManager {
    public static final int ID_LENGTH = 6;
    public static final int TRY_COUNT = 100;

    private final int MIN_PLAYER_IN_ROOM;
    private final int MAX_PLAYER_IN_ROOM;

    private Map<String, Room> rooms;

    public RoomManager() {
        MIN_PLAYER_IN_ROOM = Main.mechanicsConfig.getInt("minPlayerNumber");
        MAX_PLAYER_IN_ROOM = Main.mechanicsConfig.getInt("maxPlayerNumber");

        rooms = new ConcurrentHashMap<>();
    }

    public Collection<Room> getRooms() {
        return rooms.values();
    }

    public long getRoomCount() {
        return rooms.size();
    }

    public void destroyRoom(Room room) {
        if (room != null && rooms.containsKey(room.getId())) {
            rooms.remove(room.getId());
        }
    }

    public Room createRoom(GameService gameService, GameWebSocketHandler handler, boolean isPrivate) {
        String roomId = getUnusedRoomId();

        Room newRoom = new Room(gameService, isPrivate, roomId);

        String playerColor = newRoom.getUnusedColor();
        Player newPlayer = new Player(playerColor, handler.getUserProfile());
        newPlayer.addConnection(handler);
        newRoom.addPlayer(newPlayer);
        handler.setRoom(newRoom);

        rooms.put(roomId, newRoom);

        return newRoom;
    }

    public Room findFreePublicRoom(UserProfile user) {
        for (Room room : rooms.values()) {
            if (room.getRoomState() == Room.RoomState.GAME || room.isPrivate()) {
                continue;
            }

            Player player = room.getPlayerByUser(user);

            if (player != null) {
                // If user already in room
                return room;
            }

            if (room.getPlayerCount() < MAX_PLAYER_IN_ROOM) {
                return room;
            }
        }

        // Free room not found
        return null;
    }

    public void checkRoomReady(Room room) {
        int readyCount = room.getReadyPlayerCount();

        if (readyCount >= MIN_PLAYER_IN_ROOM && readyCount <= MAX_PLAYER_IN_ROOM
                && readyCount == room.getPlayerCount() && room.getRoomState() == Room.RoomState.WAITING) {
            room.startGame();
        }
    }

    private String getUnusedRoomId() {
        String randomId = RandomUtils.randomString(ID_LENGTH);
        int tryCount = TRY_COUNT;

        while ((tryCount--) > 0) {
            if (!rooms.containsKey(randomId)) {
                return randomId;
            }
        }

        return null;
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }
}
