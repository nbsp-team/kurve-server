package model.Bonus;

import main.Main;
import model.Bonus.Bonus;
import model.Snake.Snake;

/**
 * Created by egor on 22.04.15.
 */
public class BonusEffect {
    private static final int FPS = Integer.valueOf(Main.mechanicsConfig.FPS);
    private Snake snake;
    private Bonus.Kind kind;
    private int countDownCounter;
    public BonusEffect(Bonus.Kind kind){
        this.kind = kind;
    }
    public BonusEffect(Bonus bonus, Snake snake, int seconds){
        this(bonus.getKind());
        this.snake = snake;
        this.countDownCounter = FPS*seconds;
    }

}
