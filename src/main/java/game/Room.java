package game;

import model.UserProfile;
import websocket.message.Message;
import websocket.message.RoomPlayersMessage;

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
        broadcastMessage(new RoomPlayersMessage(this));
    }

    public void onPlayerReady(Player player) {
        player.setReady(true);
    }

    public void onPlayerDisconnect(Player player) {
        players.remove(player);
        broadcastMessage(new RoomPlayersMessage(this));
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

    public int getPlayerCount() {
        return players.size();
    }

    public List<Player> getPlayers() {
        return players;
    }

    private void startGame() {
        roomState = RoomState.GAME;
    }

    public RoomState getRoomState() {
        return roomState;
    }
}
