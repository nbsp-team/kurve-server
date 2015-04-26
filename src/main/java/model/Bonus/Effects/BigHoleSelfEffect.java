package model.Bonus.Effects;

import model.Snake.Snake;

/**
 * Created by egor on 22.04.15.
 */
public class BigHoleSelfEffect extends AbstractTemporaryEffect {
    private Snake snake;

    public BigHoleSelfEffect(Snake snake){
        this.snake = snake;
        setDuration(6);
    }

    @Override
    public void activate() {
        snake.setBigHole(true);
    }

    @Override
    public void deactivate() {
        snake.setBigHole(false);
    }
}
