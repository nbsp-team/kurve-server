package game;

import main.Main;
import model.UserProfile;
import utils.RandomUtils;
import websocket.GameWebSocketHandler;

import java.util.List;
import java.util.ArrayList;
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
    private final int DEFAULT_ROOM_CAPACITY;

    private Map<String, Room> rooms;

    public RoomManager() {
        MIN_PLAYER_IN_ROOM = Main.mechanicsConfig.getInt("minPlayerNumber");
        DEFAULT_ROOM_CAPACITY = Main.mechanicsConfig.getInt("maxPlayerNumber");

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

    public Room createRoom(GameService gameService, UserProfile user, boolean isPrivate) {
        String roomId = getUnusedRoomId();
        Room newRoom = new Room(gameService, user, DEFAULT_ROOM_CAPACITY, isPrivate, roomId);
        rooms.put(roomId, newRoom);
        return newRoom;
    }

    public Room createRoomWs(GameService gameService, GameWebSocketHandler handler, boolean isPrivate) {
        String playerColor = Room.getRandomColor();
        Player newPlayer = new Player(playerColor, handler.getUserProfile());
        newPlayer.addConnection(handler);

        Room newRoom = createRoom(gameService, handler.getUserProfile(), isPrivate);
        newRoom.addPlayer(newPlayer);
        handler.setRoom(newRoom);

        return newRoom;
    }

    public Room findFreePublicRoom(UserProfile user) {
        List<Room> acceptableRooms = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.getRoomState() == Room.RoomState.GAME || room.isPrivate()) {
                continue;
            }

            Player player = room.getPlayerByUser(user);

            if (player != null) {
                // If user already in room
                return room;
            }

            if (room.getPlayerCount() < room.getCapacity()) {
                acceptableRooms.add(room);
            }
        }

        if (acceptableRooms.size() > 0) {
            return acceptableRooms.get(RandomUtils.randInt(0, acceptableRooms.size() - 1));
        }

        // Free room not found
        return null;
    }

    public void checkRoomReady(Room room) {
        int readyCount = room.getReadyPlayerCount();

        if (readyCount >= MIN_PLAYER_IN_ROOM && readyCount <= room.getCapacity()
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
