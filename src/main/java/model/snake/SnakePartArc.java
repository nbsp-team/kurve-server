package model.snake;

import utils.Point;

/**
 * nickolay, 21.02.15.
 */
public class SnakePartArc extends SnakePart {
    enum Direction {
        left,
        light
    }

    private Point pointFrom;
    private float radius;
    private Direction direction;
    private float length;

    public SnakePartArc(Point pointFrom, float radius, Direction direction, float length) {
        this.pointFrom = pointFrom;
        this.radius = radius;
        this.direction = direction;
        this.length = length;
    }
}
