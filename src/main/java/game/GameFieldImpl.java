package game;

import interfaces.GameField;
import main.Main;
import model.Snake.Snake;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egor on 12.04.15.
 */


public class GameFieldImpl implements GameField{
    public static final int FPS = Integer.valueOf(Main.mechanicsConfig.FPS);
    public static final int width = Integer.valueOf(Main.mechanicsConfig.gameFieldWidth);
    public static final int height = Integer.valueOf(Main.mechanicsConfig.gameFieldHeight);

    private boolean playing;
    private int numPlayers, dead;
    private List<Snake> snakes;

    private  Room room;
    public GameFieldImpl(int numPlayers, Room room) {
        this.room = room;

        playing = false;
        this.numPlayers = numPlayers;

        snakes = new ArrayList<>();
        int mindim = Math.min(width, height);

        for(int i = 0; i < numPlayers; i++) {
            double angle = i*2*Math.PI/numPlayers;
            double x = width/2 + mindim*0.25*Math.cos(angle);
            double y = height/2 + mindim*0.25*Math.sin(angle);

            snakes.add(new Snake(x, y, angle+Math.PI/2, FPS, room, i));
        }
        dead = 0;
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
            run();
    }
    @Override
    public void pause(){
        playing = false;
    }

    private void step () {
        for(Snake snake : snakes) {
            if(snake.isAlive()) {
                snake.step();
                double x = snake.getX(); double y = snake.getY();
                if(x > width) {
                    snake.teleport(0, snake.getX());
                } else if(x < 0) {
                    snake.teleport(width, snake.getY());
                }
                if(y > height) {
                    snake.teleport(snake.getX(), 0);
                } else if(y < 0) {
                    snake.teleport(snake.getX(), height);
                }
                for(Snake otherSnake : snakes){
                    if(otherSnake.isInside(snake.getX()
                            ,snake.getY(), snake == otherSnake, snake.getRadius())){
                        snake.kill();
                        int lastKilled = snakes.indexOf(snake);
                        room.onPlayerDeath(lastKilled, dead);
                        dead++;


                    }
                }
            }
        }
        if(dead==numPlayers) {
            System.out.println("ALL DEAD");
            playing = false;
            room.endGame();
        }

    }

    public void run(){
        Runnable gameLoop = new Runnable(){

            @Override
            public void run(){
                long now, dt = 0;
                long last = System.nanoTime();
                long stepTime = 1000000000/FPS;
                while(playing) {
                    now = System.nanoTime();
                    dt += Math.min(1000000000, (now - last));
                    if(dt > stepTime){
                        while(dt > stepTime) {
                            dt -= stepTime;
                            step();
                        }


                    }
                    last = now;
                }
            }
        };


        Thread loopThread = new Thread(gameLoop);
        loopThread.start();

    }
}
