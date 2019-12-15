package project.ahsan.language.com.myapplication.ui.gameplay.glview.utility;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class GeometryUtils {

    private static double distance = 25.0;

    public static double getXcaculated(double angle, double speed) {
        double radian = Math.toRadians(angle);
        double dist = (distance * speed) / 100;
        double newX = dist * Math.sin(radian);
        return newX;
    }

    public static double getYcaculated(double angle, double speed) {
        double radian = Math.toRadians(angle);
        double dist = (distance * speed) / 100;
        double newY = dist * Math.cos(radian);
        return newY;
    }

    public static double getDistance(double X, double Y, double x, double y) {
        double dis1 = abs(X - x);
        dis1 = dis1 * dis1;
        double dis2 = abs(Y - y);
        dis2 = dis2 * dis2;
        double dis = sqrt(dis1 + dis2);
        return dis;
    }


}
