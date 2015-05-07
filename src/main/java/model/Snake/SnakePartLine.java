package model.Snake;

import game.MathHelper;

/**
 * Created by egor on 12.04.15.
 */
public class SnakePartLine {
    private double x1, y1, x2, y2, d, A, B, C;
    private int lineRadius, id;

    public SnakePartLine(double x, double y, double vx, double vy, int lineRadius, int id) {
        this.id = id;
        x1 = x - vx;
        y1 = y - vy;
        x2 = x1;
        y2 = y1;

        d = MathHelper.length(vx, vy);
        A = -vy / d;
        B = vx / d;
        d = 0;
        C = (-x1 * A - B * y1);


        this.lineRadius = lineRadius;
    }

    public void updateHead(double newX, double newY, double v) {
        x2 = newX;
        y2 = newY;
        d += v;
        if (MathHelper.distance(x2, y2, newX, newY) > 10) {
            System.out.println("breach");
        }
    }

    public boolean isInside(double x, double y, int lineRadius) {
        double bx = x - x1;
        double by = y1 - y;
        double proj = B * bx + A * by;

        if (Math.abs(A * x + B * y + C) >= this.lineRadius + lineRadius) return false;

        return ((proj > -lineRadius && proj < d));
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public double getlineRadius() {
        return lineRadius;
    }

    public int getId() {
        return id;
    }


}