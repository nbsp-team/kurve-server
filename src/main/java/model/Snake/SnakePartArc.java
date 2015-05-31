package model.Snake;

import utils.MathUtils;

/**
 * Created by egor on 12.04.15.
 */
public class SnakePartArc {
    private double x, y, r, angle, angle2;
    private int lineRadius, id;
    private boolean clockwise;

    public SnakePartArc(double x, double y, double radius, double startAngle
            , int lineRadius, int id, boolean clockwise, double angleV) {
        this.id = id;
        this.r = radius;
        this.x = x;
        this.y = y;
        this.clockwise = clockwise;


        this.lineRadius = lineRadius;

        angle = startAngle - angleV;
        angle2 = angle;
    }

    public void updateHead(double angleV) {
        angle2 += angleV;
        angle2 = MathUtils.normAngle(angle2);
    }

    public boolean isInside(double x, double y, int lineRadius) {

        double d = MathUtils.distance(this.x, this.y, x, y);
        if (Math.abs(d - r) > lineRadius + this.lineRadius) return false;
        double alpha = MathUtils.normAngle(Math.atan2((y - this.y), (x - this.x)));

        if (clockwise) {
            return MathUtils.isAngleBetween(alpha, angle2, angle);
        } else {
            return MathUtils.isAngleBetween(alpha, angle, angle2);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return r;
    }

    public double getLineRadius() {
        return lineRadius;
    }

    public double getAngle() {
        return angle;
    }

    public double getSpan() {
        return angle2 - angle;
    }

    public double getAngle2() {
        return angle2;
    }

    public int getId() {
        return id;
    }

    public boolean isClockwise() {
        return clockwise;
    }


}