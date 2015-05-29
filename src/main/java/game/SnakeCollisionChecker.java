package game;

import interfaces.GameField;
import model.Snake.Snake;
import model.Snake.SnakePartArc;
import model.Snake.SnakePartLine;

import java.util.List;

/**
 * Created by egor on 06.05.15.
 */
public class SnakeCollisionChecker {
    List<Snake> snakes;
    GameField field;

    SnakeCollisionChecker(List<Snake> snakes, GameField field) {
        this.snakes = snakes;
        this.field = field;
    }

    public void timeStep() {
        for (Snake snake : snakes) {
            if (snake.isAlive()) {
                if (!snake.isBigHole()) {
                    for (Snake otherSnake : snakes) {
                        if (isPointInsideSnake(otherSnake, snake.getX()
                                , snake.getY(), snake == otherSnake, snake.getRadius())) {
                            field.killSnake(snake);
                        }
                    }
                }

            }
        }
    }

    public boolean isPointInsideSnake(Snake snake, double x, double y, boolean itself, int radius) {

        List<SnakePartLine> snakeLines = snake.getSnakeLines();
        List<SnakePartArc> snakeArcs = snake.getSnakeArcs();
        int lim = snake.getSnakeLines().size();
        if (itself && !snake.isTurning()) {
            lim--;
        }
        for (int i = 0; i < lim; i++) {
            if (snakeLines.get(i).isInside(x, y, radius)) return true;
        }
        lim = snake.getSnakeArcs().size();
        if (itself && snake.isTurning()) lim--;
        for (int i = 0; i < lim; i++) {
            if (snakeArcs.get(i).isInside(x, y, radius)) return true;
        }
        return false;
    }
}
