package model.Bonus.Effects;

import main.Main;

/**
 * Created by egor on 22.04.15.
 */
public abstract class AbstractTemporaryEffect implements TemporaryEffect {
    private int countDownCounter = 0;


    protected void setDuration(int seconds) {
        countDownCounter = seconds * Main.mechanicsConfig.getInt("FPS");
    }

    public boolean timeStep() {
        countDownCounter--;
        return (countDownCounter <= 0);
    }

}
