package model.Snake;

import com.google.gson.*;
import game.Room;


import websocket.message.SnakeUpdateMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egor on 12.04.15.
 */

public class Snake {
    public static final int defaultSpeed = 100;
    public static final int defaultAngleSpeed = 87;
    public static final int defaultPartLength = 100;
    public static final int defaultHoleLength = 20;

    private double angle;

    private int id;
    private double angleV;
    private double v, vx, vy;
    private double cosV, sinV;
    private int partStopper, holeStopper;
    private double x, y;
    private double arcRadius, arcCenterX, arcCenterY;

    private List<SnakePartLine> snakeLines;
    private List<SnakePartArc> snakeArcs;
    private int stepCounter = 0;
    private boolean drawing = true, alive = true;
    private turningState turning = turningState.NOT_TURNING;
    private int radius = 4;
    private int linesSent = 0, arcsSent = 0;
    private Room room;

    public static enum turningState {
        TURNING_LEFT,
                TURNING_RIGHT,
                NOT_TURNING
    }

    public Snake(double x, double y, double angle, int FPS, Room room, int id) {
        snakeArcs = new ArrayList<>();
        snakeLines = new ArrayList<>();
        this.id = id;

        this.room=room;
        angleV = defaultAngleSpeed*2*Math.PI/180/FPS;
        v = (double)defaultSpeed / FPS;
        this.angle = angle;
        vx = v * Math.cos(angle); vy = v * Math.sin(angle);
        arcRadius = v / angleV;
        partStopper = (int)Math.round(defaultPartLength / v);
        holeStopper = (int)Math.round((defaultPartLength + defaultHoleLength) / v);

        this.x = x;	this.y = y;

        cosV = Math.cos(angleV); sinV = Math.sin(angleV);
        doLine();
    }

    public void kill() {
        alive = false;
        sendUpdates();
    }
    private  boolean idunno = false;
    public void	startTurning(turningState where) {
        idunno = true;
        if((angleV > 0) != (where == turningState.TURNING_RIGHT)) {
            angleV = -angleV;
            sinV = -sinV;
        }
        turning = where;
        doArc();
        idunno = false;
    }
    public void stopTurning(turningState where) {
        if(turning==where) {
            turning = turningState.NOT_TURNING;
            vx = v*Math.cos(angle);
            vy = v*Math.sin(angle);
            doLine();
        }
    }
    private void doArc(){
        arcsSent = Math.min(arcsSent, snakeArcs.size() - 1);
        linesSent = Math.min(linesSent, snakeLines.size() - 1);
        sendUpdates();
        boolean clockwise;
        double arcStartAngle;
        if(turning == turningState.TURNING_LEFT){
            arcStartAngle = angle + Math.PI/2;
            arcCenterX = x + arcRadius*Math.sin(angle);
            arcCenterY = y - arcRadius*Math.cos(angle);
            clockwise = true;
        } else {
            arcStartAngle = angle - Math.PI/2;
            arcCenterX = x - arcRadius*Math.sin(angle);
            arcCenterY = y + arcRadius*Math.cos(angle);
            clockwise = false;
        }


        if(!drawing) return;

        SnakePartArc newArc = new SnakePartArc(arcCenterX, arcCenterY
                , arcRadius, arcStartAngle, radius, snakeArcs.size(), clockwise);

        snakeArcs.add(newArc);

    }
    public void sendUpdates(){
        room.broadcastMessage(new SnakeUpdateMessage(this));
    }
    private void doLine() {
        linesSent = Math.min(linesSent, snakeLines.size() - 1);
        arcsSent = Math.min(arcsSent, snakeArcs.size() - 1);
        sendUpdates();

        if(!drawing) return;
        SnakePartLine newLine = new SnakePartLine(x, y, vx, vy, radius, snakeLines.size());
        snakeLines.add(newLine);
    }
    public void changeRadius(int radius) {
        this.radius = radius;
        if(drawing) {
            if(turning == turningState.TURNING_LEFT) {
                doLine();
            } else {
                doArc();
            }
        }
    }
    public boolean isInside(double x, double y, boolean itself, int radius) {

        int lim = snakeLines.size();
        if(itself && turning == turningState.NOT_TURNING) {
            lim--;
        }
        for(int i = 0; i < lim; i++){
            if(snakeLines.get(i).isInside(x, y, radius)) return true;
        }
        lim = snakeArcs.size();
        if(itself && turning != turningState.NOT_TURNING) lim--;
        for(int i = 0; i < lim; i++){
            if(snakeArcs.get(i).isInside(x, y, radius)) return true;
        }
        return false;
    }
    public void step() {
        if (idunno) return;
        makeHoles();
        if(turning == turningState.NOT_TURNING) {
            x += vx;
            y += vy;
            if(drawing) lastLine().updateHead(x, y, v);
        } else {
            double dx = (x-arcCenterX);
            double dy = (y-arcCenterY);

            angle += angleV;
            y = arcCenterY + dy*cosV + dx*sinV;
            x = arcCenterX - dy*sinV + dx*cosV;
            if(drawing) lastArc().updateHead(angleV);

        }
    }
    private void makeHoles() {
        stepCounter++;
        if(stepCounter > partStopper){
            drawing = false;
            if(stepCounter == holeStopper) {
                stepCounter = 0;
                drawing = true;
                if(turning == turningState.NOT_TURNING) {
                    doLine();
                } else {
                    doArc();
                }

            }
        }

    }
    private SnakePartLine lastLine() {
        return snakeLines.get(snakeLines.size()-1);
    }
    private SnakePartArc lastArc() {
        return snakeArcs.get(snakeArcs.size()-1);
    }

    public void teleport(double newX, double newY) {
        x = newX;	y = newY;
        if(turning == turningState.NOT_TURNING) {
            doLine();
        } else {
            doArc();
        }
    }
    public boolean isAlive(){
        return alive;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public int getRadius() {
        return radius;
    }

    public JsonObject getUpdatesJson(JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();

        JsonArray arcsToSend = new JsonArray();
        for(int i = Math.max(0, arcsSent); i < snakeArcs.size(); i++){
            arcsToSend.add(context.serialize(snakeArcs.get(i)));
        }
        JsonArray linesToSend = new JsonArray();
        for(int i = Math.max(0, linesSent); i < snakeLines.size(); i++){
            linesToSend.add(context.serialize(snakeLines.get(i)));
        }
        System.out.print(linesToSend.size());  System.out.print(" lines and ");
        System.out.print(arcsToSend.size());   System.out.println(" arcs to send");
        jsonObject.add("lines", linesToSend);
        jsonObject.add("arcs", arcsToSend);

        jsonObject.addProperty("id", id);
        jsonObject.addProperty("x", x);
        jsonObject.addProperty("y", y);
        jsonObject.addProperty("angle", angle);
        jsonObject.addProperty("nlines", snakeLines.size());
        jsonObject.addProperty("narcs", snakeArcs.size());
        jsonObject.addProperty("radius", radius);
        jsonObject.addProperty("steps", stepCounter);
        jsonObject.addProperty("alive", alive);
        if(turning != turningState.NOT_TURNING) {
            arcsSent = Math.max(0, snakeArcs.size()-1);
            linesSent = snakeLines.size();
        } else {
            arcsSent = snakeArcs.size();
            linesSent = Math.max(0, snakeLines.size()-1);
        }

        return jsonObject;
    }

}