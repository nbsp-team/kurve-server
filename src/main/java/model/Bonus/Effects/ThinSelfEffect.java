package model.Bonus.Effects;

import model.Snake.Snake;

/**
 * Created by egor on 23.04.15.
 */
public class ThinSelfEffect extends AbstractTemporaryEffect {
    private Snake snake;

    public ThinSelfEffect(Snake snake) {
        this.snake = snake;
        setDuration(15);
    }

    @Override
    public void activate() {
        snake.multiplyRadiusBy(0.5);
    }

    @Override
    public void deactivate() {
        snake.multiplyRadiusBy(2);
    }
}
