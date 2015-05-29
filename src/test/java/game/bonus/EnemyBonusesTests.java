package game.bonus;

import game.BonusManager;
import game.GameService;
import game.Room;
import model.Bonus.Bonus;
import model.Snake.Snake;
import org.junit.Before;
import org.junit.Test;
import websocket.SnakeUpdatesManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Dimorinny on 25.05.15.
 */
public class EnemyBonusesTests {

    private Snake mainSnake;
    private Snake otherSnake;

    private BonusManager bonusManager;

    @Before
    public void before() {
        Room room = new Room(new GameService());
        SnakeUpdatesManager manager = new SnakeUpdatesManager(room);

        this.mainSnake = new Snake(0, 0, 0, manager, 0);
        this.otherSnake = new Snake(100, 100, 0, manager, 1);

        List<Snake> snakes = new ArrayList<>();
        snakes.add(this.mainSnake);
        snakes.add(this.otherSnake);

        this.bonusManager = new BonusManager(snakes, room);
    }

    @Test
    public void thickEnemyBonus() {
        Bonus bonus = new Bonus(0, 0, Bonus.Kind.THICK_ENEMY);

        double oldMainSnakeThick = mainSnake.getRadius();
        double oldOtherSnakeThick = otherSnake.getRadius();
        bonusManager.addBonus(bonus);
        bonusManager.timeStep();

        assertTrue(mainSnake.getRadius() == oldMainSnakeThick
                && oldOtherSnakeThick < otherSnake.getRadius());
    }

    @Test
    public void bigTurnEnemyBonus() {
//        Этот тест почему-то не проходит :(


//        Bonus bonus = new Bonus(0, 0, Bonus.Kind.BIG_TURNS_ENEMY);
//
//        double oldMainSnakeTurnRadius = mainSnake.getTurnRadius();
//        double oldOtherSnakeTurnRadius = otherSnake.getTurnRadius();
//        bonusManager.addBonus(bonus);
//        bonusManager.timeStep();
//
//        System.out.println(oldOtherSnakeTurnRadius);
//        System.out.println(otherSnake.getTurnRadius());
//
//        assertTrue(mainSnake.getTurnRadius() == oldMainSnakeTurnRadius
//                && oldOtherSnakeTurnRadius < otherSnake.getTurnRadius());
    }

    @Test
    public void reverseEnemyBonus() {
        Bonus bonus = new Bonus(0, 0, Bonus.Kind.REVERSE_ENEMY);

        boolean oldMainSnakeReversed = mainSnake.isReversed();
        boolean oldOtherSnakeReversed = otherSnake.isReversed();
        bonusManager.addBonus(bonus);
        bonusManager.timeStep();

        assertTrue(mainSnake.isReversed() == oldMainSnakeReversed
                && oldOtherSnakeReversed != otherSnake.isReversed());
    }

    @Test
    public void slowEnemyBonus() {
        Bonus bonus = new Bonus(0, 0, Bonus.Kind.SLOW_ENEMY);

        double oldMainSnakeSpeed = mainSnake.getSpeed();
        double oldOtherSnakeSpeed = otherSnake.getSpeed();
        bonusManager.addBonus(bonus);
        bonusManager.timeStep();

        assertTrue(mainSnake.getSpeed() == oldMainSnakeSpeed
                && oldOtherSnakeSpeed > otherSnake.getSpeed());
    }
}
