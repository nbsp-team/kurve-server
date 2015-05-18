package model.Bonus.Effects;

import model.Snake.Snake;

import java.util.List;

/**
 * Created by egor on 23.04.15.
 */
public class BigTurnsEnemyEffect extends AbstractTemporaryEffect {
    private Snake snake;
    private List<Snake> snakes;
    public BigTurnsEnemyEffect(Snake eater, List<Snake> snakes) {
        this.snake = eater;
        this.snakes = snakes;
        setDuration(6);
    }

    @Override
    public void activate() {
        for(Snake other : snakes){
            if(other != snake) {
                other.multiplyTurnRadiusBy(1.25);
            }
        }
    }

    @Override
    public void deactivate() {
        for(Snake other : snakes){
            if(other != snake) other.multiplyTurnRadiusBy(0.8);
        }
    }
}
