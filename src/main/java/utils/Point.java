package utils;

import java.io.Serializable;

/**
 * nickolay, 21.02.15.
 */
public class Point implements Serializable {
    /**
     * The X coordinate of this <code>Point2D</code>.
     * @since 1.2
     * @serial
     */
    public float x;

    /**
     * The Y coordinate of this <code>Point2D</code>.
     * @since 1.2
     * @serial
     */
    public float y;

    /**
     * Constructs and initializes a <code>Point2D</code> with
     * coordinates (0,&nbsp;0).
     * @since 1.2
     */
    public Point() {
    }

    /**
     * Constructs and initializes a <code>Point2D</code> with
     * the specified coordinates.
     *
     * @param x the X coordinate of the newly
     *          constructed <code>Point2D</code>
     * @param y the Y coordinate of the newly
     *          constructed <code>Point2D</code>
     * @since 1.2
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * {@inheritDoc}
     * @since 1.2
     */
    public double getX() {
        return (double) x;
    }

    /**
     * {@inheritDoc}
     * @since 1.2
     */
    public double getY() {
        return (double) y;
    }

    /**
     * {@inheritDoc}
     * @since 1.2
     */
    public void setLocation(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    /**
     * Sets the location of this <code>Point2D</code> to the
     * specified <code>float</code> coordinates.
     *
     * @param x the new X coordinate of this {@code Point2D}
     * @param y the new Y coordinate of this {@code Point2D}
     * @since 1.2
     */
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a <code>String</code> that represents the value
     * of this <code>Point2D</code>.
     * @return a string representation of this <code>Point2D</code>.
     * @since 1.2
     */
    public String toString() {
        return "Point2D.Float["+x+", "+y+"]";
    }

    /*
     * JDK 1.6 serialVersionUID
     */
    private static final long serialVersionUID = -2870572449815403710L;
}