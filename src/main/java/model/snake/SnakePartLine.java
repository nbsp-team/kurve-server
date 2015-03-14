package model.snake;

import java.awt.*;

/**
 * nickolay, 21.02.15.
 */

public class SnakePartLine {

    private float x1, y1, y2, x2;
    private float A, B, C, d;
    private int halfWidth;

    public SnakePartLine(float x1, float y1, float vx, float vy, int width)
    {
        this.x1 = x1;
        this.x2 = x1;
        this.y1 = y1;
        this.y2 = y1;
        d = (float)Math.sqrt(vx*vx+vy*vy);
        A = -vy/d;
        B = vx/d;
        C = (-x1*A - B*y1);
        d = 0;
        halfWidth = width / 2;
    }

    public void update_head(float x2, float y2, float v) {
        this.x2 = x2;
        this.y2 = y2;
        //d = sqrt(float((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)));
        d += v;
    }

    public boolean is_inside(float x, float y) {

        float bx = x-x1;
        float by = y1-y;
        float mult = B*bx+A*by;

        if(!(mult> 0 && mult <d )) return false;

        return Math.abs(A*x+B*y+C) < halfWidth;

    }
}