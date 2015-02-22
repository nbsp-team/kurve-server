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
    private float angle;

    public SnakePartArc(Point pointFrom, float radius, Direction direction, float angle) {
        this.pointFrom = pointFrom;
        this.radius = radius;
        this.direction = direction;
        this.angle = angle;
    }

    public Point getPointFrom() {
        return pointFrom;
    }

    public void setPointFrom(Point pointFrom) {
        this.pointFrom = pointFrom;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        if (angle >= 0 && angle <= 2 * Math.PI) {
            this.angle = angle;
        } else {
            throw new IllegalArgumentException(
                    "Angle must be larger than 0 and smaller than 2*pi"
            );
        }
    }
}
