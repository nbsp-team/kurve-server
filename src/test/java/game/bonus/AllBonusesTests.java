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
public class AllBonusesTests {

    private Snake mainSnake;
    private Snake otherSnake;

    private BonusManager bonusManager;

    @Before
    public void before(){
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
    public void traverseAllBonus() {
        Bonus bonus = new Bonus(0, 0, Bonus.Kind.TRAVERSE_WALLS_ALL);

        boolean oldMainSnakeTravFlag = mainSnake.canTravThroughWalls();
        boolean oldOtherSnakeTravFlag = otherSnake.canTravThroughWalls();
        bonusManager.addBonus(bonus);
        bonusManager.timeStep();

        assertTrue(mainSnake.canTravThroughWalls() != oldMainSnakeTravFlag
                && oldOtherSnakeTravFlag != otherSnake.canTravThroughWalls());
    }
}
