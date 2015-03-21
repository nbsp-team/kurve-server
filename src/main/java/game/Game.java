package game;

import interfaces.AccountService;
import main.MemoryAccountService;
import model.UserProfile;
import websocket.GameWebSocketHandler;
import websocket.WebSocketConnection;
import websocket.message.ReadyMessage;
import websocket.message.RoomPlayersMessage;

import java.awt.*;
import java.sql.Connection;
import java.util.*;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */
public class Game implements GameWebSocketHandler.WebSocketMessageListener {
    private static final int MIN_PLAYER_IN_ROOM = 2;
    private static final int MAX_PLAYER_IN_ROOM = 6;

    @SuppressWarnings("FieldCanBeLocal")
    private AccountService accountService;

    private List<Room> rooms;

    public Game(AccountService accountService) {
        this.accountService = accountService;
        rooms = new ArrayList<>();
    }

    @Override
    public void onNewConnection(UserProfile user, WebSocketConnection connection) {
        System.out.println("New ws: " + user);

        if (user == null) {
            // TODO: close status 1
            // connection.close();

            // TEST:
            Room tmpRoom = new Room();
            tmpRoom.onNewPlayer(
                    new Player(
                            Color.BLUE,
                            new UserProfile("test", "test", "test@gmail.com")
                    )
            );
            connection.sendMessage(new RoomPlayersMessage(tmpRoom));
            return;
        }

        for (Room room : rooms) {
            Player player = room.getPlayerByUser(user);

            // If player already exist - add connection
            if (player != null) {
                player.addConnection(connection);
                return;
            }

            // If founded room with enough space
            if (room.getPlayerCount() < MAX_PLAYER_IN_ROOM) {
                connectUserToRoom(connection, user, room);
                return;
            }
        }

        // Room not found - create new
        Room newRoom = new Room();
        connectUserToRoom(connection, user, newRoom);
        rooms.add(newRoom);
    }

    private void connectUserToRoom(WebSocketConnection connection, UserProfile userProfile, Room room) {
        Color playerColor = getUnusedColor(room);
        Player newPlayer = new Player(playerColor, userProfile);
        newPlayer.addConnection(connection);
        room.onNewPlayer(newPlayer);
    }

    @Override
    public void onDisconnect(UserProfile user) {
        if (user == null) {
            return;
        }

        Room userRoom = findPlayerRoom(user);
        if (userRoom != null) {
            Player player = userRoom.getPlayerByUser(user);
            userRoom.onPlayerDisconnect(player);
        }
    }

    @Override
    public void onUserReady(UserProfile user, boolean isReady) {
        if (user != null) {
            Room room = findPlayerRoom(user);
            if (room != null) {
                int playerId = room.getPlayerIdByUser(user);
                room.getPlayers().get(playerId).setReady(isReady);
                room.broadcastMessageExceptUser(new ReadyMessage(
                        playerId,
                        isReady
                ), user);
            }
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

    private Room findPlayerRoom(UserProfile user) {
        for(Room room : rooms) {
            Player player = room.getPlayerByUser(user);
            if (player != null) {
                if (player.getUserProfile().getLogin().equals(user.getLogin())) {
                    return room;
                }
            }
        }
        return null;
    }
}