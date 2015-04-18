package model.Snake;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

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
        double d = Math.sqrt((this.x-x)*(this.x-x)+(this.y-y)*(this.y-y));
        if(Math.abs(d-r)>lineRadius + this.lineRadius) return false;
        double alpha = Math.atan2((y-this.y),(x-this.x));

        return ((alpha<angle) != (alpha< angle + span));
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

    public static class serializer implements JsonSerializer<SnakePartArc> {
        public JsonElement serialize(SnakePartArc src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("id", src.getId());
            jsonObject.addProperty("x", src.getX());
            jsonObject.addProperty("y", src.getY());
            jsonObject.addProperty("angle", src.getAngle());
            jsonObject.addProperty("span", src.getSpan());
            jsonObject.addProperty("radius", src.getRadius());
            jsonObject.addProperty("lineRadius", src.getLineRadius());
            jsonObject.addProperty("clockwise", src.isClockwise());
            return jsonObject;
        }
    }
}