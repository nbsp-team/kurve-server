package game;

import main.AccountService;
import model.UserProfile;
import org.eclipse.jetty.websocket.api.Session;
import utils.SessionManager;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */
public class Game {
    private static final int MIN_PLAYER_IN_ROOM = 2;
    private static final int MAX_PLAYER_IN_ROOM = 6;

    private AccountService accountService;
    private final SessionManager sessionManager;

    private List<Room> rooms;

    public Game(AccountService accountService, SessionManager sessionManager) {
        this.accountService = accountService;
        this.sessionManager = sessionManager;
        rooms = new ArrayList<>();
    }

    public void onNewConnection(String sessionId, Session session) {
        System.out.println("New ws: " + sessionId);
        UserProfile user = getUserBySessionId(sessionId);

        if (user == null) {
            // TODO: close status 1
            // session.close();
            return;
        }

        for (Room room : rooms) {
            Player player = room.getPlayerByUser(user);

            // If player already exist - connect session
            if (player != null) {
                player.connectSession(session);
                return;
            }

            // If founded room with enough places
            if (room.getPlayerCount() < MAX_PLAYER_IN_ROOM) {
                Player newPlayer = new Player(Color.BLUE, user);
                newPlayer.connectSession(session);
                room.onNewPlayer(newPlayer);
                return;
            }
        }

        // Room not found - create new
        Room newRoom = new Room();
        Player newPlayer = new Player(Color.BLUE, user);
        newPlayer.connectSession(session);
        newRoom.onNewPlayer(newPlayer);
        rooms.add(newRoom);
    }

    public void onDisconnect(String sessionId) {
        UserProfile user = getUserBySessionId(sessionId);

        if (user == null) {
            return;
        }

        Room userRoom = findPlayerRoom(user);
        if (userRoom != null) {
            Player player = userRoom.getPlayerByUser(user);
            userRoom.onPlayerDisconnect(player);
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

    public void onReady(String sessionId) {
        UserProfile user = getUserBySessionId(sessionId);

        if (user != null) {
            Room room = findPlayerRoom(user);
            if (room != null) {
                room.getPlayerByUser(user).setReady(true);
            }
        }
    }

    private UserProfile getUserBySessionId(String sessionId) {
        Optional<HttpSession> session = sessionManager.getSessionById(sessionId);

        if (session.isPresent()) {
            UserProfile user = accountService.getUser((String) session.get().getAttribute("username"));
            return user;
        } else {
            return null;
        }
    }
}