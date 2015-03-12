package utils;

import java.io.Serializable;

/**
 * nickolay, 21.02.15.
 */
public class Point implements Serializable {
    private float x;
    private float y;

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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String toString() {
        return "Point.Float[" + x + ", " + y + "]";
    }
}