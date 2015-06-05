package game;

import interfaces.GameField;
import main.Main;
import model.Snake.Snake;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import websocket.SnakeUpdatesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egor on 12.04.15.
 */


public class GameFieldImpl implements GameField {
    public static final Logger LOG = LogManager.getLogger(GameService.class);

    public static final int FPS = Integer.valueOf(Main.mechanicsConfig.FPS);
    public static final int SECOND = 1_000_000_000;
    public static final int STEP_TIME = SECOND / FPS;
    public static final int width = Integer.valueOf(Main.mechanicsConfig.gameFieldWidth);
    public static final int height = Integer.valueOf(Main.mechanicsConfig.gameFieldHeight);
    public static final int LAST_DEATH_DELAY = 1700;

    private final GameService gameService;
    private final SnakeUpdatesManager updatesManager;

    private boolean playing;
    private int numPlayers, dead;
    private List<Snake> snakes;
    private BonusManager bonusManager;
    private SnakeCollisionChecker snakeCollisionChecker;

    private Room room;
    private boolean lastDeath = false;
    private long lastDeathTimeMillis;

    public GameFieldImpl(Room room, GameService gameService) {
        updatesManager = new SnakeUpdatesManager(room);
        this.room = room;
        this.gameService = gameService;

        playing = false;
        this.numPlayers = room.getPlayerCount();

        snakes = new ArrayList<>();
        int mindim = Math.min(width, height);

        for (int i = 0; i < numPlayers; i++) {
            double angle = i * 2 * Math.PI / numPlayers;
            double x = width / 2 + mindim * 0.25 * Math.cos(angle);
            double y = height / 2 + mindim * 0.25 * Math.sin(angle);

            Snake snake = new Snake(x, y, angle + Math.PI / 2, updatesManager, i);
            snakes.add(snake);
        }

        dead = 0;
        bonusManager = new BonusManager(snakes, room);
        snakeCollisionChecker = new SnakeCollisionChecker(snakes, this);

    }

    @Override
    public void doLeftDown(int sender) {
        snakes.get(sender).startTurning(Snake.turningState.TURNING_LEFT);
    }

    @Override
    public void doLeftUp(int sender) {
        snakes.get(sender).stopTurning(Snake.turningState.TURNING_LEFT);
    }

    @Override
    public void doRightDown(int sender) {
        snakes.get(sender).startTurning(Snake.turningState.TURNING_RIGHT);
    }

    @Override
    public void doRightUp(int sender) {
        snakes.get(sender).stopTurning(Snake.turningState.TURNING_RIGHT);
    }

    @Override
    public void play() {
        playing = true;

        new Thread(() -> {
            try {
                Thread.sleep(Integer.valueOf(Main.mechanicsConfig.gameStartCountdown) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            run();
        }).start();
    }

    @Override
    public void pause() {
        playing = false;
    }

    private void teleportOrKill(Snake snake, double x, double y) {
        if (snake.canTravThroughWalls()) {
            snake.teleport(x, y);
        } else {
            killSnake(snake);
        }
    }

    public void killSnake(Snake snake) {
        snake.kill();
        int lastKilled = snakes.indexOf(snake);
        room.onPlayerDeath(lastKilled, dead);
        dead++;
    }


    public void killAll() {
        snakes.stream().filter(Snake::isAlive).forEach(this::killSnake);
    }

    @Override
    public SnakeUpdatesManager getUpdatesManager() {
        return updatesManager;
    }

    private void step(double frames) {
        snakes.stream().filter(Snake::isAlive).forEach(snake -> {
            snake.step(frames);

            double x = snake.getX();
            double y = snake.getY();
            if (x > width) {
                teleportOrKill(snake, 0, snake.getY());
            } else if (x < 0) {
                teleportOrKill(snake, width, snake.getY());
            }
            if (y > height) {
                teleportOrKill(snake, snake.getX(), 0);
            } else if (y < 0) {
                teleportOrKill(snake, snake.getX(), height);
            }
        });

        snakeCollisionChecker.timeStep();
        bonusManager.timeStep();

        if (dead >= numPlayers - 1 && !lastDeath) {
            LOG.debug("Round over");
            lastDeath = true;
            lastDeathTimeMillis = System.currentTimeMillis();
        }

        if (lastDeath) {
            if ((System.currentTimeMillis() - lastDeathTimeMillis) >= LAST_DEATH_DELAY) {
                killAll();
                playing = false;
                room.startRound();
            }
        }
    }

    public void run() {
        Runnable gameLoop = () -> {
            long now, dt = 0;
            long last = System.nanoTime();
            long stepTime = STEP_TIME;
            while (playing) {
                now = System.nanoTime();
                dt = Math.min(SECOND, (now - last));
                step((double)dt / stepTime);

                long sleepNano = (stepTime - (System.nanoTime() - now) );
                if (sleepNano > 0) {
                    try {
                        Thread.sleep(sleepNano / 1000_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                last = now;
            }
        };

        Thread loopThread = new Thread(gameLoop);
        loopThread.start();
    }
}
