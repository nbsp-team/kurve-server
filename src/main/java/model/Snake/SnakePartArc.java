package model.Snake;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import game.MathHelper;

import java.lang.reflect.Type;

/**
 * Created by egor on 12.04.15.
 */
public class SnakePartArc{
    private double x, y, r, angle, span;
    private int lineRadius, id;
    private boolean clockwise;

    public SnakePartArc(double x, double y, double radius, double startAngle, int lineRadius, int id, boolean clockwise) {
        this.id = id;
        this.r = radius;
        this.x = x;
        this.y = y;
        this.clockwise = clockwise;

        span = 0;
        this.lineRadius = lineRadius;

        angle = startAngle;

    }
    public void updateHead(double angleV) {
        span += angleV;
    }
    public boolean isInside(double x, double y, int lineRadius){
        double d = MathHelper.distance(this.x, this.y, x, y);
        if(Math.abs(d-r)>lineRadius + this.lineRadius) return false;
        double alpha = Math.atan2((y-this.y),(x-this.x));

        return MathHelper.isBetween(alpha, angle, angle + span);
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getRadius(){
        return r;
    }
    public double getLineRadius(){
        return lineRadius;
    }

    public double getAngle() {
        return angle;
    }

    public double getSpan() {
        return span;
    }

    public int getId() {
        return id;
    }

    public boolean isClockwise() {
        return clockwise;
    }


}