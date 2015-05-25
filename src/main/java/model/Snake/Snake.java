package model.Snake;

import utils.MathHelper;
import game.Room;
import main.Main;
import websocket.SnakeUpdatesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egor on 12.04.15.
 */

public class Snake {
    public static final int defaultSpeed = Integer.valueOf(Main.mechanicsConfig.snakeDefaultSpeed);
    public static final int defaultTurnRadius = Integer.valueOf(Main.mechanicsConfig.snakeDefaultTurnRadius);
    public static final int FPS = Integer.valueOf(Main.mechanicsConfig.FPS);
    public static final int holeLength = Integer.valueOf(Main.mechanicsConfig.snakeHoleLength);
    public static final int minPartLength = Integer.valueOf(Main.mechanicsConfig.snakeMinPartLength);
    public static final int maxPartLength = Integer.valueOf(Main.mechanicsConfig.snakeMaxPartLength);
    private double angle;

    private int id;
    private double angleV;
    private double v, vx, vy;
    private double cosV, sinV;
    private int partStopper, holeStopper;
    private double x, y;
    private double turnRadius, arcCenterX, arcCenterY;

    private List<SnakePartLine> snakeLines;
    private List<SnakePartArc> snakeArcs;
    private double travSinceLastHole = 0;
    private boolean drawing = true, alive = true;
    private turningState turning = turningState.NOT_TURNING;
    private int radius = Integer.valueOf(Main.mechanicsConfig.defaultSnakeWidth) / 2;
    private int linesSent = 0, arcsSent = 0;

    private SnakeUpdatesManager updatesManager;
    private boolean bigHole = false;
    private boolean travThroughWalls = false;



    public void setArcsSent(int arcsSent) {
        this.arcsSent = arcsSent;
    }

    public void setLinesSent(int linesSent) {
        this.linesSent = linesSent;
    }

    public static enum turningState {
        TURNING_LEFT,
        TURNING_RIGHT,
        NOT_TURNING
    }

    public Snake(double x, double y, double angle, SnakeUpdatesManager updatesManager, int id) {
        snakeArcs = new ArrayList<>();
        snakeLines = new ArrayList<>();
        this.id = id;
	    this.updatesManager = updatesManager;


        v = (double) defaultSpeed / FPS;
        angleV = v / defaultTurnRadius;
        this.angle = angle;
        vx = v * Math.cos(angle);
        vy = v * Math.sin(angle);
        turnRadius = defaultTurnRadius;


        partStopper = MathHelper.randInt(minPartLength, maxPartLength);
        holeStopper = partStopper + holeLength;

        this.x = x;
        this.y = y;

        cosV = Math.cos(angleV);
        sinV = Math.sin(angleV);
        doLine();
    }

    public void kill() {
        alive = false;
        sendUpdates();
    }

    private boolean reversed = false;
    public synchronized void startTurning(turningState where) {

        if(reversed && where != turningState.NOT_TURNING) where = (where == turningState.TURNING_LEFT)?turningState.TURNING_RIGHT:turningState.TURNING_LEFT;
        if ((angleV > 0) != (where == turningState.TURNING_RIGHT)) {
            angleV = -angleV;
            sinV = -sinV;
        }
        turning = where;
        doArc();

    }

    public synchronized void stopTurning(turningState where) {

        if(reversed && where != turningState.NOT_TURNING) where = (where == turningState.TURNING_LEFT)?turningState.TURNING_RIGHT:turningState.TURNING_LEFT;
        if (turning == where) {
            turning = turningState.NOT_TURNING;
            angle = MathHelper.normAngle(angle);
            vx = v * Math.cos(angle);
            vy = v * Math.sin(angle);

            doLine();
        }

    }

    private void doArc() {
        arcsSent = Math.min(arcsSent, snakeArcs.size() - 1);
        linesSent = Math.min(linesSent, snakeLines.size() - 1);
        sendUpdates();
        boolean clockwise;
        double arcStartAngle;
        if (turning == turningState.TURNING_LEFT) {
            arcStartAngle = MathHelper.normAngle(angle + Math.PI / 2);
            arcCenterX = x + turnRadius * Math.sin(angle);
            arcCenterY = y - turnRadius * Math.cos(angle);
            clockwise = true;
        } else {
            arcStartAngle = MathHelper.normAngle(angle - Math.PI / 2);
            arcCenterX = x - turnRadius * Math.sin(angle);
            arcCenterY = y + turnRadius * Math.cos(angle);
            clockwise = false;
        }


        if (!drawing) return;

        SnakePartArc newArc = new SnakePartArc(arcCenterX, arcCenterY
                , turnRadius, arcStartAngle, radius, snakeArcs.size(), clockwise, angleV);

        snakeArcs.add(newArc);
    }
    public void sendUpdates() {
	    updatesManager.broadcast(this);
    }

    private void doLine() {
        linesSent = Math.min(linesSent, snakeLines.size() - 1);
        arcsSent = Math.min(arcsSent, snakeArcs.size() - 1);
        sendUpdates();

        if (!drawing) return;
        SnakePartLine newLine = new SnakePartLine(x, y, vx, vy, radius, snakeLines.size());
        snakeLines.add(newLine);
    }

    public void multiplyRadiusBy(double koef) {
        radius *= koef;
        doNewPart();
    }

    public synchronized void step() {

        makeHoles();
        if (turning == turningState.NOT_TURNING) {
            x += vx;
            y += vy;
            if (drawing) lastLine().updateHead(x, y, v);
        } else {
            double dx = (x - arcCenterX);
            double dy = (y - arcCenterY);

            angle += angleV;
            y = arcCenterY + dy * cosV + dx * sinV;
            x = arcCenterX - dy * sinV + dx * cosV;
            if (drawing) lastArc().updateHead(angleV);

        }

    }

    private void makeHoles() {
        travSinceLastHole += v;
        if (travSinceLastHole > partStopper) {

            if (travSinceLastHole >= holeStopper) {
                travSinceLastHole = 0;
                partStopper = MathHelper.randInt(minPartLength, maxPartLength);
                holeStopper = partStopper + holeLength;
                drawing = true;
                doNewPart();
            } else drawing = false;
        }

    }

    private SnakePartLine lastLine() {
        return snakeLines.get(snakeLines.size() - 1);
    }

    private SnakePartArc lastArc() {
        return snakeArcs.get(snakeArcs.size() - 1);
    }

    public void multiplyTurnRadiusBy(double koef) {
        turnRadius *= koef;
        angleV /= koef;
        cosV = Math.cos(angleV);
        sinV = Math.sin(angleV);
        if (turning != turningState.NOT_TURNING) {
            doArc();
        } else
            sendUpdates();
    }

    public void reverse(){
        if(reversed){
            startTurning(turning);
            reversed = !reversed;
        } else {
            reversed = !reversed;
            startTurning(turning);
        }
    }

    public void multiplySpeedBy(double koef) {
        v *= koef;
        vx *= koef;
        vy *= koef;
        angleV *= koef;
        cosV = Math.cos(angleV);
        sinV = Math.sin(angleV);
        //turnRadius *= koef;
        if (turning != turningState.NOT_TURNING) {
            doArc();
        } else sendUpdates();
    }

    private void doNewPart() {
        if (turning == turningState.NOT_TURNING) {
            doLine();
        } else {
            doArc();
        }
    }

    public void teleport(double newX, double newY) {
        x = newX;
        y = newY;
        doNewPart();
    }

    public boolean isAlive() {
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


    public void eraseSelf() {
        snakeArcs.clear();
        snakeLines.clear();
        doNewPart();
//        sendUpdates();
    }

    public void setBigHole(boolean bigHole) {
        this.bigHole = bigHole;
    }

    public boolean isBigHole() {
        return bigHole;
    }

    public void setTravThroughWalls(boolean travThroughWalls) {
        this.travThroughWalls = travThroughWalls;
    }

    public boolean canTravThroughWalls() {
        return travThroughWalls;
    }

    public List<SnakePartLine> getSnakeLines() {
        return snakeLines;
    }

    public List<SnakePartArc> getSnakeArcs() {
        return snakeArcs;
    }

    public boolean isTurning() {
        return turning != turningState.NOT_TURNING;
    }

    public int getArcsSent() {
        return arcsSent;
    }

    public int getLinesSent() {
        return linesSent;
    }

    public int getId() {
        return id;
    }

    public double getAngle() {
        return angle;
    }

    public double getAngleSpeed() {
        return angleV;
    }

    public double getSpeed() {
        return v;
    }

    public double getTravSinceLastHole() {
        return travSinceLastHole;
    }

    public double getTurnRadius() {
        return turnRadius;
    }

    public int getPartStopper() {
        return partStopper;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }
}
