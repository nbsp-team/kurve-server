package model.Bonus.Effects;

/**
 * Created by egor on 22.04.15.
 */
public interface TemporaryEffect {
    public void activate();

    public void deactivate();

    boolean timeStep();
}
