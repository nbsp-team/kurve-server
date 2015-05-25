package game;

import model.Snake.Snake;
import org.junit.Before;
import org.junit.Test;
import websocket.SnakeUpdatesManager;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertTrue;
/**
 * Created by egor on 07.05.15.
 */
public class SnakeTests {

    private Snake snake;

    @Before
    public void before() {
        Room room = new Room(new GameManager());
        SnakeUpdatesManager manager = new SnakeUpdatesManager(room);

        this.snake = new Snake(0, 0, 0, manager, 0);
    }
    @Test
    public void testTurning(){
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
        assertTrue(snake.getSnakeArcs().size() == 0);
        assertTrue(snake.getSnakeLines().size() == 1);
        snake.startTurning(Snake.turningState.TURNING_LEFT);
        assertTrue(snake.getSnakeArcs().size() != 0);
        assertTrue(snake.getSnakeLines().size() == 1);
        snake.stopTurning(Snake.turningState.TURNING_LEFT);
        assertTrue(snake.getSnakeLines().size() == 2);
    }
}
