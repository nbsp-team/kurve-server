package game;

import model.Snake.Snake;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertTrue;
/**
 * Created by egor on 07.05.15.
 */
public class SnakeTests {
    private Room room;
    @Before
    public void before(){
        this.room = new Room(new GameManager());
    }
    @Test
    public void testTurning(){

        Snake snake = new Snake(0, 0, 0, room, 0);
        assertTrue(!snake.isTurning());
        snake.stopTurning(Snake.turningState.TURNING_LEFT);
        assertTrue(!snake.isTurning());
        snake.startTurning(Snake.turningState.TURNING_RIGHT);
        assertTrue(snake.isTurning());
        snake.startTurning(Snake.turningState.TURNING_RIGHT);
        assertTrue(snake.isTurning());
        snake.stopTurning(Snake.turningState.TURNING_LEFT);
        assertTrue(snake.isTurning());
        snake.stopTurning(Snake.turningState.TURNING_RIGHT);
        assertTrue(!snake.isTurning());
    }

    @Test
    public void testParts(){
        Snake snake = new Snake(0, 0, 0, room, 0);
        assertTrue(snake.getSnakeArcs().size() == 0);
        assertTrue(snake.getSnakeLines().size() == 1);
        snake.startTurning(Snake.turningState.TURNING_LEFT);
        assertTrue(snake.getSnakeArcs().size() != 0);
        assertTrue(snake.getSnakeLines().size() == 1);
        snake.stopTurning(Snake.turningState.TURNING_LEFT);
        assertTrue(snake.getSnakeLines().size() == 2);
    }
}
