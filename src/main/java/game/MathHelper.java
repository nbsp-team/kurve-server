package game;

/**
 * Created by egor on 19.04.15.
 */
public class MathHelper {
    public static double distance(double x1, double y1, double x2, double y2){
        return length(x2-x1, y2-y1);
    }
    public static double length(double dx, double dy){
        return Math.sqrt(dx * dx + dy * dy);
    }
    public static boolean isBetween(double x, double lim1, double lim2){
        return ((x<lim1) != (x< lim2));
    }
    public static boolean isAngleBetween(double alpha, double lim1, double lim2){
        return (lim2 > lim1) == ( (alpha > lim1) != (alpha > lim2) );
    }
    public static double normAngle(double x){
        while(x >= 2*Math.PI) x -= 2*Math.PI;
        while(x < 0) x += 2*Math.PI;
        return x;
    }
    public static double shortDouble(double a){
        return Math.floor(16*a)/16f;

    }
}
