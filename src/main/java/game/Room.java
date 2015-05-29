package game;

import interfaces.GameField;
import model.UserProfile;
import websocket.GameWebSocketHandler;
import websocket.message.*;

import java.util.ArrayList;
import java.util.List;

/**
 * nickolay, 21.02.15.
 */
public class Room {
    public static final int ROUND_NUMBER = 6;

    enum RoomState {
        WAITING,
        GAME;
    }

    private List<Player> players;

    private RoomState roomState = RoomState.WAITING;
    private GameField gameField;
    private final GameService gameService;

    private int currentRound = 0;

    private int getPointsByDeathId(int deathId) {
        return deathId + 1;
    }

    public void onPlayerDeath(int playerId, int deathId) {
        if (playerId >= players.size()) return;
        players.get(playerId).setPoints(getPointsByDeathId(deathId));
    }

    public Room(GameService gameService) {
        players = new ArrayList<>();

        this.gameService = gameService;
    }

    public void onNewPlayer(Player player) {
        if (roomState != RoomState.WAITING) return;

        players.add(player);
        player.sendMessage(new RoomPlayersMessage(this));
        broadcastMessageExceptUser(new ConnectedPlayerMessage(player,
                getPlayerIdByUser(player.getUserProfile())), player.getUserProfile());
    }

    public void onPlayerReady(Player player, boolean isReady) {
        if (roomState != RoomState.WAITING) return;
        player.setReady(isReady);

        broadcastMessageExceptUser(
                new ReadyMessage(player, isReady),
                player.getUserProfile()
        );
    }

    public void onKeyEvent(boolean isLeft, boolean isUp, int playerId) {
        if (roomState != RoomState.GAME) return;

        if (isLeft) {
            if (isUp) {
                gameField.doLeftUp(playerId);
            } else {
                gameField.doLeftDown(playerId);
            }
        } else {
            if (isUp) {
                gameField.doRightUp(playerId);
            } else {
                gameField.doRightDown(playerId);
            }
        }

    }

    public void startRound() {
        if (currentRound < ROUND_NUMBER) {
            currentRound++;

            for (int i = 0; i < players.size(); i++) {
                players.get(i).sendMessage(new StartRoundMessage(this, i));
            }

            roomState = RoomState.GAME;
            gameField = new GameFieldImpl(this, gameService);
            gameField.play();
        } else {
            endGame();
            gameService.destroyRoom(this);
        }
    }

    public void endGame() {
        broadcastMessage(new GameOverMessage(this));
    }

    public void startGame() {
        if (roomState != RoomState.WAITING) return;

        roomState = RoomState.GAME;

        for (int i = 0; i < players.size(); i++) {
            players.get(i).sendMessage(new StartGameMessage(this, i));
        }
        gameField = new GameFieldImpl(this, gameService);

        gameField.play();
    }

    public void onPlayerDisconnect(Player player) {
        broadcastMessageExceptUser(
                new DisconnectedPlayerMessage(player),
                player.getUserProfile()
        );
        players.remove(player);
    }

    public void broadcastMessage(Message message) {
        for (Player player : players) {
            player.sendMessage(message);
        }
    }

    public void broadcastMessageExceptUser(Message message, UserProfile user) {
        for (Player player : players) {
            String roomUserId = player.getUserProfile().getId();
            if (!roomUserId.equals(user.getId())) {
                player.sendMessage(message);
            }
        }
    }

    public void broadcastMessageExceptConnection(Message message, GameWebSocketHandler connection) {
        for (Player player : players) {
            player.sendMessageExceptConnection(message, connection);
        }
    }

    public Player getPlayerByUser(UserProfile userProfile) {
        for (Player player : players) {
            if (player.getUserProfile().getId().equals(userProfile.getId())) {
                return player;
            }
        }
        return null;
    }

    public int getPlayerIdByUser(UserProfile userProfile) {
        int index = 0;
        for (Player player : players) {
            if (player.getUserProfile().getId().equals(userProfile.getId())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public boolean isColorUsed(String color) {
        for (Player p : players) {
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
        for (Player p : players) {
            if (p.isReady()) {
                readyUserCount++;
            }
        }
        return readyUserCount;
    }

    public void sendPatchToUser(UserProfile user, List<Integer> lostIds) {
        getPlayerByUser(user).sendMessage(new SnakePatchMessage(gameField.getUpdatesManager().getListByIds(lostIds)));
    }

    public List<Player> getPlayers() {
        return players;
    }

    public RoomState getRoomState() {
        return roomState;
    }

    public GameField getGameField() {
        return gameField;
    }
}
