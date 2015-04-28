package model.Bonus.Effects;

import model.Snake.Snake;

/**
 * Created by egor on 22.04.15.
 */
public class SlowSelfEffect extends AbstractTemporaryEffect {
    private Snake snake;

    public SlowSelfEffect(Snake snake) {
        this.snake = snake;
        setDuration(10);
    }

    @Override
    public void activate() {
        snake.multiplySpeedBy(0.5);
        snake.multiplyTurnRadiusBy(2.5);
    }

    @Override
    public void deactivate() {
        snake.multiplyTurnRadiusBy(0.4);
        snake.multiplySpeedBy(2);
    }
}
