package model.Bonus;

import main.Main;
import model.Snake.Snake;
import utils.MathUtils;

/**
 * Created by egor on 22.04.15.
 */
public class Bonus {
    private static int next_id = 0;

    private int id;


    public enum Kind {
        SPEED_SELF,
        THIN_SELF,
        SLOW_SELF,
        BIG_HOLE_SELF,
        TRAVERSE_WALLS_SELF,
        SHARP_CORNERS_SELF,
        ERASE_SELF,
        SPEED_ENEMY,
        THICK_ENEMY,
        SLOW_ENEMY,
        REVERSE_ENEMY,
        BIG_TURNS_ENEMY,
        TRAVERSE_WALLS_ALL,
        DEATH_ALL
    }

    private Kind kind;
    private double x, y;
    private static final int radius = Main.mechanicsConfig.getInt("bonusRadius");

    public Bonus(int x, int y, Kind kind) {
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

    public boolean isReachableBy(Snake snake) {
        return radius + snake.getRadius() > MathUtils.distance(snake.getX(),
                snake.getY(), x, y);
    }


}
