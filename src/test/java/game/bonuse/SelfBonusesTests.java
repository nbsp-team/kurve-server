package game.bonuse;

import game.BonusManager;
import game.GameManager;
import game.Room;
import model.Bonus.Bonus;
import model.Snake.Snake;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by egor on 07.05.15.
 */
public class SelfBonusesTests {
    private Snake snake;
    private BonusManager bonusManager;

    @Before
    public void before(){
        Room room = new Room(new GameManager());
        this.snake = new Snake(0, 0, 0, room, 0);
        List<Snake> snakes = new ArrayList<>();
        snakes.add(snake);
        this.bonusManager = new BonusManager(snakes, room);
    }

    @Test
    public void testSpeedSelfBonus(){
        Bonus bonus = new Bonus(0, 0, Bonus.Kind.SPEED_SELF);

        double oldSpeed = snake.getSpeed();
        bonusManager.addBonus(bonus);
        bonusManager.timeStep();
        assertTrue(snake.getSpeed() > oldSpeed);
    }

    @Test
    public void testSlowSelfBonus() {
        Bonus bonus = new Bonus(0, 0, Bonus.Kind.SLOW_SELF);

        double oldSpeed = snake.getSpeed();
        bonusManager.addBonus(bonus);
        bonusManager.timeStep();
        assertTrue(snake.getSpeed() < oldSpeed);
    }

    @Test
    public void testThinSelfBonus() {
        Bonus bonus = new Bonus(0, 0, Bonus.Kind.THIN_SELF);

        int oldRadius = snake.getRadius();
        bonusManager.addBonus(bonus);
        bonusManager.timeStep();
        assertTrue(snake.getRadius() < oldRadius);
    }

    @Test
    public void testBigHoleSelfBonus() {
        Bonus bonus = new Bonus(0, 0, Bonus.Kind.BIG_HOLE_SELF);

        boolean oldBigHoleFlag = snake.isBigHole();
        bonusManager.addBonus(bonus);
        bonusManager.timeStep();
        assertTrue(oldBigHoleFlag != snake.isBigHole());
    }

    @Test
    public void testTraverseWallsSelfBonus() {
        Bonus bonus = new Bonus(0, 0, Bonus.Kind.TRAVERSE_WALLS_SELF);

        boolean oldTraverseWallsFlag = snake.canTravThroughWalls();
        bonusManager.addBonus(bonus);
        bonusManager.timeStep();
        assertTrue(oldTraverseWallsFlag != snake.canTravThroughWalls());
    }
}
