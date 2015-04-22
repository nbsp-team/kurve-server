package model.Bonus.Effects;

import model.Snake.Snake;

/**
 * Created by egor on 22.04.15.
 */
public interface TemporaryEffect {
    public void activate();
    public void deactivate();

    boolean timeStep();
}
