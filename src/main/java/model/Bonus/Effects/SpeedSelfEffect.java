package model.Bonus.Effects;

import model.Snake.Snake;

/**
 * Created by egor on 22.04.15.
 */
public class SpeedSelfEffect extends AbstractTemporaryEffect {
    private Snake snake;

    public SpeedSelfEffect(Snake snake) {
        this.snake = snake;
        setDuration(3);
    }

    @Override
    public void activate() {
        snake.multiplySpeedBy(2);
       // snake.multiplyTurnRadiusBy(2);
    }

    @Override
    public void deactivate() {
        //snake.multiplyTurnRadiusBy(0.5);
        snake.multiplySpeedBy(0.5);
    }
}
