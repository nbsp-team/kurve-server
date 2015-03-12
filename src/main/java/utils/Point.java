package utils;

import java.io.Serializable;

/**
 * nickolay, 21.02.15.
 */
public class Point implements Serializable {
    public float x;
    public float y;

    public Point() {
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String toString() {
        return "Point.Float[" + x + ", " + y + "]";
    }
}