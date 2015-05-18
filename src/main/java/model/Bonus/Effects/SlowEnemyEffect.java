package model.Bonus.Effects;

import model.Snake.Snake;

import java.util.List;

/**
 * Created by egor on 23.04.15.
 */
public class SlowEnemyEffect extends AbstractTemporaryEffect {
    private Snake snake;
    private List<Snake> snakes;
    public SlowEnemyEffect(Snake eater, List<Snake> snakes) {
        this.snake = eater;
        this.snakes = snakes;
        setDuration(5);
    }

    @Override
    public void activate() {
        for(Snake other : snakes){
            if(other != snake) {
                other.multiplySpeedBy(0.5);
            }
        }
    }

    @Override
    public void deactivate() {
        for(Snake other : snakes){
            if(other != snake) other.multiplySpeedBy(2);
        }
    }
}
