package game;

/**
 * Created by egor on 19.04.15.
 */
public class MathHelper {
    public static double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }
    public static boolean isBetween(double x, double lim1, double lim2){
        return ((x<lim1) != (x< lim2));
    }
}
