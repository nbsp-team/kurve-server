package game;

import dao.ScoresDao;
import model.Score;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import websocket.GameWebSocketHandler;
import websocket.message.*;

import java.util.Collection;
import java.util.Date;

/**
 * nickolay, 21.02.15.
 */
public class GameService implements GameWebSocketHandler.WebSocketMessageListener {
    public static final Logger LOG = LogManager.getLogger(GameService.class);

    private final ScoresDao scoresDao;
    private RoomManager roomManager;

    public GameService(ScoresDao scoresDao) {
        roomManager = new RoomManager();
        this.scoresDao = scoresDao;
    }

    @Override
    public void onNewConnection(GameWebSocketHandler handler) {
        System.out.println("New WebSocket connection: " + handler.getUserProfile());
        System.out.println("Rooms: " + getRoomCount());

        if (handler.getUserProfile() == null) {
            System.out.println("Disconnect: auth required");
            handler.disconnect(GameWebSocketHandler.CLOSE_REASON_NO_AUTH , "Auth required");
        }
    }

    public void destroyRoom(Room room) {
        roomManager.destroyRoom(room);
    }

    @Override
    public void onDisconnect(GameWebSocketHandler handler) {
        if (handler.getUserProfile() == null) {
            return;
        }

        Room userRoom = handler.getRoom();

        if (userRoom != null) {
            Player player = userRoom.getPlayerByUser(handler.getUserProfile());
            if (player == null) {
                return;
            }

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
                roomManager.checkRoomReady(room);

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

    @Override
    public void onConnectToRoom(GameWebSocketHandler handler, String roomId) {
        if (roomId == null || roomId.length() == 0) {
            // Connect to public room
            Room freeRoom = roomManager.findFreePublicRoom(handler.getUserProfile());
            if (freeRoom == null) {
                return;
            }

            freeRoom.connect(handler);
        } else {
            // Connect to private room
            Room privateRoom = roomManager.getRoom(roomId);
            if (privateRoom != null) {
                privateRoom.connect(handler);
            } else {
                handler.sendMessage(new ConnectedToRoomMessage(""));
            }
        }
    }

    @Override
    public void onGetRooms(GameWebSocketHandler handler) {
        // TODO: send rooms message
    }

    public void writePointsToDb(Room room) {
        room.getPlayers().stream().filter(player -> player.getPoints() > 0).forEach(player -> {
            scoresDao.insert(
                    new Score(player.getUserProfile().getId(), player.getPoints(), new Date())
            );
        });
    }

    public long getRoomCount() {
        return roomManager.getRoomCount();
    }

    public Collection<Room> getRooms() {
        return roomManager.getRooms();
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }
}