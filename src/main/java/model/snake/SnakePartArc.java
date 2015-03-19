package model.snake;



public class SnakePartArc {
    private float y, x, r;
    private float angle, span;
    private int halfWidth;

    public SnakePartArc(float x, float y, float r, float startAngle, float span, int width){

        this.r = r;
        this.x = x;
        this.y = y;
        angle = startAngle;
        this.span = span;
        halfWidth = width / 2;
    }

    public void update_head(float span) {
        this.span = span;
    }
    public boolean is_inside(float x, float y) {


        double d = Math.sqrt((x-this.x)*(x-this.x)+(y-this.y)*(y-this.y));
        if(Math.abs(d-r)>halfWidth) return false;
        double alpha = Math.atan2((y-this.y),(x-this.x));


        return ((alpha<=angle) != (alpha< angle + span));

    }

}