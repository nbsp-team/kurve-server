package model.Bonus;

import game.MathHelper;
import model.Snake.Snake;

/**
 * Created by egor on 22.04.15.
 */
public class Bonus {
    private static int next_id = 0;

    private int id;
    public static enum Kind {
        SPEED_SELF,
        THIN_SELF,
        ERASE_SELF
    }
    private Kind kind;
    private double x, y;
    private static final int radius = 10;

    public Bonus(int x, int y, Kind kind){
        this.x = x;
        this.y = y;
        this.kind = kind;
        id = next_id;
        next_id++;
    }

    public int getId() {
        return id;
    }

    public Kind getKind() {
        return kind;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isReachableBy(Snake snake){
        return radius > MathHelper.distance(snake.getX(), snake.getY(), x, y);
    }


}
