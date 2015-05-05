package model.Bonus;

import game.MathHelper;
import model.Snake.Snake;

/**
 * Created by egor on 22.04.15.
 */
public class Bonus {
    private static int next_id = 0;

    private int id;
    private String color = "#33EE33";

    public String getColor() {
        return color;
    }

    public static enum Kind {
        SPEED_SELF,
        THIN_SELF,
        ERASE_SELF,
        SLOW_SELF,
        BIG_HOLE_SELF,
        TRAVERSE_WALLS_SELF
    }

    private Kind kind;
    private double x, y;
    private static final int radius = 10;

    public Bonus(int x, int y, Kind kind) {
        this.x = x;
        this.y = y;
        this.kind = kind;
        id = next_id;
        next_id++;
        initColor();
    }

    private void initColor() {
        switch (kind) {
            case BIG_HOLE_SELF:
                color = "#11EEFF";
                break;

        }
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

    public boolean isReachableBy(Snake snake) {
        return radius > MathHelper.distance(snake.getX(), snake.getY(), x, y);
    }


}
