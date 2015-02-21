package game;

import main.AccountService;
import model.UserProfile;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */
public class Game {
    private static final int MAX_PLAYER_IN_ROOM = 6;
    private AccountService accountService;

    private List<Room> rooms;

    public Game() {
        accountService = new AccountService();
        rooms = new ArrayList<>();
    }

    public void onNewConnection(String sessionId) {
        UserProfile user = accountService.getSessions(sessionId);

        for(Room room : rooms) {
            Player player = room.getPlayerByUser(user);

            // If player already exist - connect session
            if (player != null) {
                player.connectSession(sessionId);
                return;
            }

            // If founded room with enough places
            if (room.getPlayerCount() < MAX_PLAYER_IN_ROOM) {
                Player newPlayer = new Player(Color.BLUE, user);
                room.onNewPlayer(newPlayer);
                return;
            }
        }

        // Room not found - create new
        Room newRoom = new Room();
        Player newPlayer = new Player(Color.BLUE, user);
        newRoom.onNewPlayer(newPlayer);
        rooms.add(newRoom);
    }
}