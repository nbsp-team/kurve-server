package model.snake;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

/**
 * nickolay, 21.02.15.
 */

public class Snake
{
    private List<SnakePartLine> snakeLines;
    private List<SnakePartArc> snakeArcs;


    private boolean alive;
    private float x, y, vx, vy, angle;
    private float v, angleV;
    private int partStopper, holeStopper, stepCounter;

    private TurnState turning;
    public enum TurnState {
         TURNING_LEFT,
        TURNING_RIGHT,
        NOT_TURNING
    }
    private float arcCenterX, arcCenterY;
    private float arcRadius, arcAngle, arcStartAngle;
    private static final int DEFAULT_SNAKE_WIDTH = 6;
    private static final int DEFAULT_HOLE_LENGTH = 5;
    private static final int DEFAULT_PART_LENGTH = 20;

    public Snake(float x, float y, float speed, float angleSpeed, float angle)
    {
        this.x = x;
        this.y = y;

        v = speed;
        this.angle = angle;
        angleV = angleSpeed;
        turning = TurnState.NOT_TURNING;
        vx = v*(float)Math.cos(angle);
        vy = v*(float)Math.sin(angle);
        stepCounter = 0;

        setPartLength(DEFAULT_PART_LENGTH);
        setHoleLength(DEFAULT_HOLE_LENGTH);

        alive = true;
        arcRadius = v/angleV;
        width = DEFAULT_SNAKE_WIDTH;

        snakeLines = new ArrayList<>(0);
        snakeArcs = new ArrayList<>(0);
    }

    public void start_turning(TurnState where) {
        turning = where;
        doArc();
    }
    public void stop_turning(TurnState where){
        if(turning==where) {
            turning = TurnState.NOT_TURNING;
            vx = v*(float)Math.cos(angle);
            vy = v*(float)Math.sin(angle);
            doLine();
        }
    }
    private void doArc(){

        if(turning == TurnState.TURNING_LEFT){
            arcStartAngle = angle;
        } else {
            arcStartAngle = angle - (float)Math.PI/2;
        }
        arcStartAngle = normAngle(arcStartAngle);
        arcCenterX = x - arcRadius*(float)Math.cos(arcStartAngle);
        arcCenterY = y - arcRadius*(float)Math.sin(arcStartAngle);


        arcAngle = arcStartAngle;
        if(!drawing()) return;
        SnakePartArc newArc = new SnakePartArc(arcCenterX, arcCenterY, arcRadius, arcStartAngle, 0, width);
        snakeArcs.add(newArc);
        sendUpdatesToEveryone();
    }
    private void doLine(){
        if(!drawing()) return;
        SnakePartLine newLine = new SnakePartLine(x, y, vx, vy, width);
        snakeLines.add(newLine);
        sendUpdatesToEveryone();
    }
    public void teleport(float x, float y) {
        this.x = x;
        this.y = y;
        if(turning == TurnState.NOT_TURNING) {
            doLine();
        } else {
            doArc();
        }
    }
    public void step() {

        makeHoles();

        if(turning == TurnState.NOT_TURNING) {
            x += vx;
            y += vy;
            if(drawing()) lastLine().update_head(x, y, v);
        } else {
            if (turning == TurnState.TURNING_LEFT) {
                angle -= angleV;
                arcAngle -= angleV;
            } else {
                angle += angleV;
                arcAngle += angleV;
            }
            angle = normAngle(angle);
            arcAngle = normAngle(arcAngle);
            x = arcCenterX + arcRadius*(float)Math.cos(arcAngle);
            y = arcCenterY + arcRadius*(float)Math.sin(arcAngle);

            if(drawing()) lastArc().update_head(arcAngle-arcStartAngle);
        }
    }
    public boolean is_inside(float x, float y, boolean itself) {

        boolean res = false;

        int lim = snakeLines.size();
        if(itself && turning == TurnState.NOT_TURNING) lim--;
        for(int i = 0; i < lim; i++) {
            if(snakeLines.get(i).is_inside(x, y)) res = true;
        }

        lim = snakeArcs.size();
        if(itself && turning != TurnState.NOT_TURNING) lim--;
        for(int i = 0; i < lim; i++) {
            if(snakeArcs.get(i).is_inside(x, y)) res = true;
        }
        return res;
    }

    public void setPartLength(int len) {
        partStopper = (int)(len / v);
    }
    public void setHoleLength(int len) {
        holeStopper = (int)(partStopper + len/v);
    }
    public float getX() { return x; }
    public float getY() { return y; }
    public void setWidth(int w) { width = w; }
    public boolean isAlive() { return alive;}
    public void setAlive(boolean alive) {this.alive = alive;}


    private float normAngle(float angle) {

        angle = angle % 2*(float)Math.PI;

        if(angle < 0) angle += 2*Math.PI;
        return angle;
    }
    private void makeHoles() {
        if(stepCounter > holeStopper) {
            stepCounter = 0;
            if(turning == TurnState.NOT_TURNING) {
                doLine();
            } else {
                doArc();
            }
        }
        stepCounter++;
    }
    private boolean drawing() {
        return stepCounter < partStopper;
    }
    private SnakePartArc lastArc() {
        return snakeArcs.get(snakeArcs.size() - 1);
    }
    private SnakePartLine lastLine() {
        return snakeLines.get(snakeLines.size() - 1);
    }
    private void sendUpdatesToEveryone() {
        // send new parts to all players
    }


    private int width;
}
