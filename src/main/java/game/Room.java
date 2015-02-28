package game;

import model.UserProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */
public class Room {
    enum RoomState {
        waiting,
        game
    }

    private List<Player> players;
    private RoomState roomState = RoomState.waiting;

    public Room() {
        players = new ArrayList<>();
    }

    public void onNewPlayer(Player player) {
        players.add(player);
    }

    public void onPlayerReady(Player player) {
        player.setReady(true);
    }

    public void onPlayerDisconnect(Player player) {
        players.remove(player);
    }

    public Player getPlayerByUser(UserProfile userProfile) {
        for(Player player : players) {
            if (player.getUserProfile().getId() == userProfile.getId()) {
                return player;
            }
        }
        return null;
    }

    public int getPlayerCount() {
        return players.size();
    }

    private void startGame() {
        roomState = RoomState.game;
    }

    public RoomState getRoomState() {
        return roomState;
    }
}
