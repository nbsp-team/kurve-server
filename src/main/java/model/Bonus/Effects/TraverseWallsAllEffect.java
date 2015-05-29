package model.Bonus.Effects;

import model.Snake.Snake;

import java.util.List;

/**
 * Created by egor on 23.04.15.
 */
public class TraverseWallsAllEffect extends AbstractTemporaryEffect {

    private List<Snake> snakes;

    public TraverseWallsAllEffect(List<Snake> snakes) {

        this.snakes = snakes;
        setDuration(10);
    }

    @Override
    public void activate() {
        for (Snake snake : snakes) snake.setTravThroughWalls(true);
    }

    @Override
    public void deactivate() {
        for (Snake snake : snakes) snake.setTravThroughWalls(false);
    }
}
