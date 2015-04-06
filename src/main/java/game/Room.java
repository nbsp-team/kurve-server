package game;

import model.UserProfile;
import websocket.message.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */
public class Room {
    enum RoomState {
        WAITING,
        GAME
    }

    private List<Player> players;
    private RoomState roomState = RoomState.WAITING;

    public Room() {
        players = new ArrayList<>();
    }

    public void onNewPlayer(Player player) {
        players.add(player);
        player.sendMessage(new RoomPlayersMessage(this));
        broadcastMessageExceptUser(new ConnectedPlayerMessage(player,
                getPlayerIdByUser(player.getUserProfile())), player.getUserProfile());

    }

    public void onPlayerReady(Player player, boolean isReady) {
        player.setReady(isReady);
        broadcastMessageExceptUser(
                new ReadyMessage(player, isReady),
                player.getUserProfile()
        );
    }

    public void onPlayerDisconnect(Player player) {
        broadcastMessageExceptUser(
                new DisconnectedPlayerMessage(player),
                player.getUserProfile()
        );
        players.remove(player);
    }

    public void broadcastMessage(Message message) {
        for(Player player : players) {
            player.sendMessage(message);
        }
    }

    public void broadcastMessageExceptUser(Message message, UserProfile user) {
        for(Player player : players) {
            String roomUserLogin = player.getUserProfile().getLogin();
            if (!roomUserLogin.equals(user.getLogin())) {
                player.sendMessage(message);
            }
        }
    }

    public Player getPlayerByUser(UserProfile userProfile) {
        for(Player player : players) {
            if (player.getUserProfile().getLogin().equals(userProfile.getLogin())) {
                return player;
            }
        }
        return null;
    }

    public int getPlayerIdByUser(UserProfile userProfile) {
        for(int i = 0; i < players.size(); ++i) {
            if (players.get(i).getUserProfile().getLogin().equals(userProfile.getLogin())) {
                return i;
            }
        }
        return -1;
    }

    public boolean isColorUsed(Color color) {
        for(Player p : players) {
            if (p.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public int getReadyPlayerCount() {
        int readyUserCount = 0;
        for(Player p : players) {
            if (p.isReady()) {
                readyUserCount++;
            }
        }
        return readyUserCount;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void startGame() {
        roomState = RoomState.GAME;
        broadcastMessage(
                new StartGameMessage(this)
        );
    }

    public RoomState getRoomState() {
        return roomState;
    }
}
