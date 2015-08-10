package game;

import interfaces.GameField;
import main.Main;
import model.UserProfile;
import utils.RandomUtils;
import websocket.GameWebSocketHandler;
import websocket.message.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * nickolay, 21.02.15.
 */
public class Room implements Comparable<Room> {
    public static final int ROUND_NUMBER = 6;
    private final long creationDate;
    private final boolean isPrivate;
    private final String id;
    private static List<String> playerColors;

    @Override
    public int compareTo(Room o) {
        return o.getId().compareTo(o.getId());
    }

    enum RoomState {
        WAITING,
        GAME
    }

    private Map<String, Player> players;
    private UserProfile owner;

    private RoomState roomState = RoomState.WAITING;
    private GameField gameField;
    private final GameService gameService;

    private int currentRound = 0;

    public Room(GameService gameService, UserProfile owner, boolean isPrivate, String id) {
        this.players = new ConcurrentHashMap<>();
        this.creationDate = System.currentTimeMillis();
        this.gameService = gameService;
        this.isPrivate = isPrivate;
        this.id = id;
        this.owner = owner;
        this.playerColors = Main.mechanicsConfig.getStringList("snake.colors");
    }

    private int getPointsByDeathId(int deathId) {
        return deathId;
    }

    public void onPlayerDeath(int playerId, int deathId) {
        if (playerId >= players.size()) return;

        int playerPoints = players.get(playerId).getPoints() + getPointsByDeathId(deathId);
        players.get(playerId).setPoints(playerPoints);

        broadcastMessage(new RatingUpdateMessage(this));
    }

    public void addPlayer(Player player) {
        if (roomState != RoomState.WAITING) return;

        players.put(player.getId(), player);
        player.sendMessage(new RoomPlayersMessage(this));
        broadcastMessage(new ConnectedPlayerMessage(player,
                getPlayerIdByUser(player.getUserProfile())));
    }

    public void connect(GameWebSocketHandler handler) {
        Player player = getPlayerByUser(handler.getUserProfile());
        if (player != null) {
            player.addConnection(handler);
            handler.sendMessage(new RoomPlayersMessage(this));
        } else {
            String playerColor = getUnusedColor();
            Player newPlayer = new Player(playerColor, handler.getUserProfile());
            newPlayer.addConnection(handler);
            addPlayer(newPlayer);
            handler.setRoom(this);
        }
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
        System.out.println("[Room] startRound()");

        if (currentRound < ROUND_NUMBER - 1) {
            currentRound++;

            for (int i = 0; i < players.size(); i++) {
                players.get(i).sendMessage(new StartRoundMessage(this, i, currentRound, ROUND_NUMBER));
            }

            roomState = RoomState.GAME;
            gameField = new GameFieldImpl(this, gameService);
            gameField.play();
        } else {
            System.out.println("[Room] Конец игры! Раунды закончились");

            endGame();
        }
    }

    public void endGame() {
        gameService.writePointsToDb(this);
        broadcastMessage(new GameOverMessage(this));

        if (gameField != null) {
            gameField.pause();
        }
        gameService.destroyRoom(this);
    }

    public void startGame() {
        if (roomState != RoomState.WAITING) return;

        roomState = RoomState.GAME;

        for (int i = 0; i < players.size(); i++) {
            players.get(i).sendMessage(new StartGameMessage(this, i, currentRound, ROUND_NUMBER));
        }

        broadcastMessage(new RatingUpdateMessage(this));

        gameField = new GameFieldImpl(this, gameService);

        gameField.play();
    }

    public void onPlayerDisconnect(Player player) {
        players.remove(player);

        try {
            broadcastMessageExceptUser(
                    new DisconnectedPlayerMessage(player),
                    player.getUserProfile()
            );
        } catch (Exception e) {
            System.out.println("[Room] Disconnected player message sending error!");
        }

        if (players.size() == 0) {
            System.out.println("[Room] Конец игры: 0 игроков, удаление комнаты!");
            endGame();
        }
    }

    public void broadcastMessage(Message message) {
        for (Player player : players.values()) {
            player.sendMessage(message);
        }
    }

    public void broadcastMessageExceptUser(Message message, UserProfile user) {
        for (Player player : players.values()) {
            String roomUserId = player.getUserProfile().getId();
            if (!roomUserId.equals(user.getId())) {
                player.sendMessage(message);
            }
        }
    }

    public void broadcastMessageExceptConnection(Message message, GameWebSocketHandler connection) {
        for (Player player : players.values()) {
            player.sendMessageExceptConnection(message, connection);
        }
    }

    public Player getPlayerByUser(UserProfile userProfile) {
        for (Player player : players.values()) {
            if (player.getUserProfile().getId().equals(userProfile.getId())) {
                return player;
            }
        }
        return null;
    }

    public int getPlayerIdByUser(UserProfile userProfile) {
        int index = 0;
        for (Player player : players.values()) {
            if (player.getUserProfile().getId().equals(userProfile.getId())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public boolean isColorUsed(String color) {
        for (Player p : players.values()) {
            if (p.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }

    public String getUnusedColor() {
        for (String c : playerColors) {
            if (!isColorUsed(c)) {
                return c;
            }
        }
        return "#000000";
    }

    public static String getRandomColor() {
        return playerColors.get(RandomUtils.randInt(0, playerColors.size()));
    }

    public int getPlayerCount() {
        return players.size();
    }

    public int getReadyPlayerCount() {
        int readyUserCount = 0;
        for (Player p : players.values()) {
            if (p.isReady()) {
                readyUserCount++;
            }
        }
        return readyUserCount;
    }

    public void sendPatchToUser(UserProfile user, List<Integer> lostIds) {
        getPlayerByUser(user).sendMessage(new SnakePatchMessage(gameField.getUpdatesManager().getListByIds(lostIds)));
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public RoomState getRoomState() {
        return roomState;
    }

    public GameField getGameField() {
        return gameField;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getId() {
        return id;
    }

    public UserProfile getOwner() {
        return owner;
    }
}
