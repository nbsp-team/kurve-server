package game;

import main.AccountService;
import model.UserProfile;
import utils.SessionManager;
import websocket.GameWebSocketHandler;
import websocket.WebSocketConnection;
import websocket.message.RoomPlayersMessage;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */
public class Game implements GameWebSocketHandler.WebSocketMessageListener {
    private static final int MIN_PLAYER_IN_ROOM = 2;
    private static final int MAX_PLAYER_IN_ROOM = 6;

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
                Player newPlayer = new Player(Color.BLUE, user);
                newPlayer.addConnection(connection);
                room.onNewPlayer(newPlayer);
                return;
            }
        }

        // Room not found - create new
        Room newRoom = new Room();
        Player newPlayer = new Player(Color.BLUE, user);
        newPlayer.addConnection(connection);
        newRoom.onNewPlayer(newPlayer);
        rooms.add(newRoom);
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
    public void onUserReady(UserProfile user) {
        if (user != null) {
            Room room = findPlayerRoom(user);
            if (room != null) {
                room.getPlayerByUser(user).setReady(true);
            }
        }
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