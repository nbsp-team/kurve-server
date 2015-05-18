package model.Bonus.Effects;

import model.Snake.Snake;

/**
 * Created by egor on 07.05.15.
 */
public class SharpCornersSelfEffect extends AbstractTemporaryEffect {
    private Snake snake;

    public SharpCornersSelfEffect(Snake snake) {
        this.snake = snake;
        setDuration(15);
    }

    @Override
    public void activate() {
        snake.multiplyTurnRadiusBy(0.5);
    }

    @Override
    public void deactivate() {
        snake.multiplyTurnRadiusBy(2);
    }
}
