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

        for(int i = 0; i < rooms.size(); ++i) {
            Room room = rooms.get(i);
            Player player = room.getPlayerByUser(user);

            // If player already exist - connect session
            if (player != null) {
                player.connectSession(sessionId);
                user.setCurrentRoomId(i);
                return;
            }

            // If founded room with enough places
            if (room.getPlayerCount() < MAX_PLAYER_IN_ROOM) {
                Player newPlayer = new Player(Color.BLUE, user);
                room.onNewPlayer(newPlayer);
                user.setCurrentRoomId(i);
                return;
            }
        }

        // Room not found - create new
        Room newRoom = new Room();
        Player newPlayer = new Player(Color.BLUE, user);
        newRoom.onNewPlayer(newPlayer);
        rooms.add(newRoom);
        user.setCurrentRoomId(rooms.size() - 1);
    }

    public void onDisconnect(String sessionId) {
        UserProfile user = accountService.getSessions(sessionId);
        if (user.getRoomId() != -1) {
            Room room = rooms.get(user.getRoomId());
            Player player = room.getPlayerByUser(user);
            room.onPlayerDisconnect(player);
        }
    }

    public void onReady(String sessionId) {
        UserProfile user = accountService.getSessions(sessionId);
        int roomId = user.getRoomId();
        Room room = rooms.get(roomId);
        room.getPlayerByUser(user).setReady(true);
    }
}