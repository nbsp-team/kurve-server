package game;

import main.AccountService;
import model.UserProfile;
import org.eclipse.jetty.server.session.AbstractSessionIdManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * nickolay, 21.02.15.
 */
public class Game {
    private static final int MAX_PLAYER_IN_ROOM = 6;
    private AccountService accountService;

    // TODO: rewrite
    private List<Room> rooms;
    private Map<UserProfile, Room> userRooms;

    public Game(AccountService accountService) {
        this.accountService = accountService;
        rooms = new ArrayList<>();
    }

    public void onNewConnection(String sessionId) {
        UserProfile user = null; // TODO: get user by sessionId

        for (Room room : rooms) {
            Player player = room.getPlayerByUser(user);

            // If player already exist - connect session
            if (player != null) {
                player.connectSession(sessionId);
                userRooms.put(user, room);
                return;
            }

            // If founded room with enough places
            if (room.getPlayerCount() < MAX_PLAYER_IN_ROOM) {
                Player newPlayer = new Player(Color.BLUE, user);
                room.onNewPlayer(newPlayer);
                userRooms.put(user, room);
                return;
            }
        }

        // Room not found - create new
        Room newRoom = new Room();
        Player newPlayer = new Player(Color.BLUE, user);
        newRoom.onNewPlayer(newPlayer);
        rooms.add(newRoom);
        userRooms.put(user, newRoom);;
    }

    public void onDisconnect(String sessionId) {
        UserProfile user = null; // TODO: get user by sessionId
        Room userRoom = userRooms.get(user);
        if (userRoom != null) {
            Player player = userRoom.getPlayerByUser(user);
            userRoom.onPlayerDisconnect(player);
        }
    }

    public void onReady(String sessionId) {
        UserProfile user = null; // TODO: get user by sessionId
        Room room = userRooms.get(user);
        room.getPlayerByUser(user).setReady(true);
    }
}