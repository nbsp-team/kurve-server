package model.Bonus.Effects;

import model.Snake.Snake;

import java.util.List;

/**
 * Created by egor on 23.04.15.
 */
public class ReverseEnemyEffect extends AbstractTemporaryEffect {
    private Snake snake;
    private List<Snake> snakes;
    public ReverseEnemyEffect(Snake eater, List<Snake> snakes) {
        this.snake = eater;
        this.snakes = snakes;
        setDuration(5);
    }

    @Override
    public void activate() {
        for(Snake other : snakes){
            if(true || other != snake) {
                other.reverse();
            }
        }
    }

    @Override
    public void deactivate() {
        for(Snake other : snakes){
            other.reverse();
        }
    }
}
