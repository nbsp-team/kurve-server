package game;

import model.Bonus.Bonus;
import model.Snake.Snake;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by egor on 07.05.15.
 */
public class BonusManagerTests {
    private Room room;
    private Snake snake;
    private BonusManager bonusManager;
    private List<Snake> snakes;
    @Before
    public void before(){

        this.room = new Room();
        this.snake = new Snake(0, 0, 0, room, 0);
        snakes = new ArrayList<>();
        snakes.add(snake);
        this.bonusManager = new BonusManager(snakes, room);
    }
    @Test
    public void testBonus(){
        Bonus bonus = new Bonus(0, 0, Bonus.Kind.SPEED_SELF);

        double oldSpeed = snake.getSpeed();
        bonusManager.addBonus(bonus);
        bonusManager.timeStep();
        assertTrue(snake.getSpeed() > oldSpeed);
    }
}
