package game;

import main.Main;
import model.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import service.Address;
import service.Request;
import service.Response;
import service.Service;
import utils.Bundle;
import websocket.GameWebSocketHandler;
import websocket.WebSocketConnection;
import websocket.message.ConnectedPlayerMessage;
import websocket.message.ControlMessage;
import websocket.message.RoomPlayersMessage;
import websocket.message.StartGameMessage;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */
public class GameService extends Service implements GameWebSocketHandler.WebSocketMessageListener {
    private final Address address = new Address();
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

        WebSocketConnection connection = handler.getConnection();

        if (handler.getUserProfile() == null) {
            connection.disconnect(WebSocketConnection.CLOSE_REASON_NO_AUTH, "Auth required");
            return null;
        }

        for (Room room : rooms) {
            if (room.getRoomState() == Room.RoomState.GAME) {
                continue;
            }

            Player player = room.getPlayerByUser(handler.getUserProfile());

            if (player != null) {
                player.addConnection(connection);
                player.sendMessage(new RoomPlayersMessage(room));
                room.broadcastMessageExceptUser(new ConnectedPlayerMessage(player,
                        room.getPlayerIdByUser(player.getUserProfile())), player.getUserProfile());
                return room;
            }

            if (room.getPlayerCount() < MAX_PLAYER_IN_ROOM) {
                connectUserToRoom(connection, handler, room);
                return room;
            }
        }

        Room newRoom = new Room(this);
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
                player.removeConnection(handler.getConnection());
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
                    handler.getConnection().sendMessage(
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
                    handler.getConnection()
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

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    protected Response processRequest(Request request) {
        switch (request.getMethod()) {
            case "on_new_connection":
                Bundle args = request.getArgs();
                GameWebSocketHandler handler = (GameWebSocketHandler) args.getSerializable("handler");
                Bundle bundle = new Bundle();
                Room room = onNewConnection(handler);
                bundle.putSerializable("room", room);
                return new Response(bundle);
            case "on_disconnect":
                args = request.getArgs();
                handler = (GameWebSocketHandler) args.getSerializable("handler");
                onDisconnect(handler);
                return null;
            case "on_user_ready":
                args = request.getArgs();
                handler = (GameWebSocketHandler) args.getSerializable("handler");
                boolean ready = args.getBoolean("ready");
                onUserReady(handler, ready);
                return null;
            case "on_control":
                args = request.getArgs();
                handler = (GameWebSocketHandler) args.getSerializable("handler");
                boolean left = args.getBoolean("left");
                boolean up = args.getBoolean("up");
                onControl(handler, left, up);
                return null;
        }

        return null;
    }
}