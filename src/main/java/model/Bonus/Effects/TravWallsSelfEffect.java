package model.Bonus.Effects;

import model.Snake.Snake;

/**
 * Created by egor on 22.04.15.
 */
public class TravWallsSelfEffect extends AbstractTemporaryEffect {
    private Snake snake;

    public TravWallsSelfEffect(Snake snake) {
        this.snake = snake;
        setDuration(15);
    }

    @Override
    public void activate() {
        snake.setTravThroughWalls(true);
    }

    @Override
    public void deactivate() {
        snake.setTravThroughWalls(false);
    }
}
