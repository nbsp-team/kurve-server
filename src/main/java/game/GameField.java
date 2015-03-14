package game;

import model.snake.Snake;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameField {
    private boolean playing;
    private List<Snake> snakes;
    private int fieldWidth, fieldHeight, numPlayers;
    private long milliSecPerStep;
    private static final int MAX_FPS = 30;
    private static final float DEFAULT_SNAKE_SPEED = 5;
    private static final float DEFAULT_SNAKE_ANGLE_SPEED = 0.1f;

    public GameField( int width, int height, int numOfPlayers){
        fieldWidth = width;
        fieldHeight = height;
        numPlayers = numOfPlayers;
        snakes = new ArrayList<>(0);

        for(int i = 0; i < numPlayers; i++) {
            int screenQuarter = Math.min(fieldWidth, fieldHeight) / 4;
            float angle = i*2*(float)Math.PI/numPlayers;
            float x = fieldWidth/2 + screenQuarter*(float)Math.cos(angle);
            float y = fieldHeight/2 + screenQuarter*(float)Math.sin(angle);
            Snake newSnake = new Snake(x, y, DEFAULT_SNAKE_SPEED/MAX_FPS, DEFAULT_SNAKE_ANGLE_SPEED/MAX_FPS, angle);
            snakes.add(newSnake);
        }
        milliSecPerStep = 1000 / MAX_FPS;
    }


    public void startGame() {
        if (playing) return;
        playing = true;

        Runnable gameLoop = new Runnable(){

            @Override
            public void run(){
                while(playing) {
                    long startTime = System.nanoTime();
                    step();
                    long nanoSecPassed = System.nanoTime() - startTime;
                    int milliSecPassed = (int)(nanoSecPassed / 1000000);
                    if(milliSecPassed < milliSecPerStep) {
                        try{
                            Thread.sleep(milliSecPerStep - milliSecPassed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };


        Thread loopThread = new Thread(gameLoop);
        loopThread.start();
    }
    public void pauseGame(){
        playing = false;
    }



    public static final int LEFT_DOWN_EVENT = 1;
    public static final int RIGHT_DOWN_EVENT = 2;
    public static final int LEFT_UP_EVENT = 3;
    public static final int RIGHT_UP_EVENT = 4;
    public void do_event(int sender, int event) {
        switch (event){
            case LEFT_DOWN_EVENT: {
                snakes.get(sender).start_turning(Snake.TurnState.TURNING_LEFT);
            } break;
            case RIGHT_DOWN_EVENT: {
                snakes.get(sender).start_turning(Snake.TurnState.TURNING_RIGHT);
            } break;
            case LEFT_UP_EVENT: {
                snakes.get(sender).stop_turning(Snake.TurnState.TURNING_LEFT);
            } break;
            case RIGHT_UP_EVENT: {
                snakes.get(sender).stop_turning(Snake.TurnState.TURNING_RIGHT);
            } break;

        }
    }
    private void step() {
        int i = 0;
        for(Snake snake : snakes) {
            if(snake.isAlive()){
                snake.step();
                float x = snake.getX();
                float y = snake.getY();

                // if out of bounds:
                if (x > fieldWidth) {
                    snake.teleport(0, y);
                } else if(x < 0) {
                    snake.teleport(fieldWidth, y);
                }
                if (y > fieldHeight) {
                    snake.teleport(x, 0);
                } else if(y < 0) {
                    snake.teleport(x, fieldHeight);
                }

                //checking for collisions:
                int ii = 0;
                for(Snake otherSnake : snakes) {
                    if(otherSnake.is_inside(x, y, ii==i)){
                        snake.setAlive(false);
                    }
                    ii++;
                }
            }
            i++;
        }
    }
}
