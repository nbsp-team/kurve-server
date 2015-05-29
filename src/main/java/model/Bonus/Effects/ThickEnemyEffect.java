package model.Bonus.Effects;

import model.Snake.Snake;

import java.util.List;

/**
 * Created by egor on 23.04.15.
 */
public class ThickEnemyEffect extends AbstractTemporaryEffect {
    private Snake snake;
    private List<Snake> snakes;

    public ThickEnemyEffect(Snake eater, List<Snake> snakes) {
        this.snake = eater;
        this.snakes = snakes;
        setDuration(7);
    }

    @Override
    public void activate() {
        for (Snake other : snakes) {
            if (other != snake) other.multiplyRadiusBy(2);
        }
    }

    @Override
    public void deactivate() {
        for (Snake other : snakes) {
            if (other != snake) other.multiplyRadiusBy(0.5);
        }
    }
}
