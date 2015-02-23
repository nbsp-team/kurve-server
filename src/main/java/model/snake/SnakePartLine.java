package model.snake;

import java.awt.*;

/**
 * nickolay, 21.02.15.
 */
public class SnakePartLine extends SnakePart {
    private Point pointFrom;
    private Point pointTo;

    public SnakePartLine(Point pointFrom, Point pointTo) {
        this.pointFrom = pointFrom;
        this.pointTo = pointTo;
    }

    public Point getPointFrom() {
        return pointFrom;
    }

    public Point getPointTo() {
        return pointTo;
    }
}
