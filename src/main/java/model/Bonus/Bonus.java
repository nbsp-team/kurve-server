package model.Bonus;

import game.MathHelper;
import model.Snake.Snake;

/**
 * Created by egor on 22.04.15.
 */
public class Bonus {
    public static enum Kind {
        SPEED_SELF,
        THIN_SELF
    }
    private Kind kind;
    private int x, y;
    private static final int radius = 10;

    public Bonus(int x, int y, Kind kind){
        this.x = x;
        this.y = y;
        this.kind = kind;
    }

    public Kind getKind() {
        return kind;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isReachableBy(Snake snake){
        return radius > MathHelper.distance(snake.getX(), snake.getY(), x, y);
    }
}
