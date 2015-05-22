package model.Bonus;

import utils.MathHelper;
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

    public enum Kind {
        SPEED_SELF,
        THIN_SELF,
        ERASE_SELF,
        SLOW_SELF,
        BIG_HOLE_SELF,
        TRAVERSE_WALLS_SELF,
        SHARP_CORNERS_SELF,
        SPEED_ENEMY,
        THICK_ENEMY,
        SLOW_ENEMY,
        BIG_TURNS_ENEMY,
        TRAVERSE_WALLS_ALL,
        DEATH_ALL,
        REVERSE_ENEMY
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
        return radius + snake.getRadius() > MathHelper.distance(snake.getX(), snake.getY(), x, y);
    }


}
