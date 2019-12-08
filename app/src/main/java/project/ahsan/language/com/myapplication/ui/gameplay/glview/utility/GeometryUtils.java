package project.ahsan.language.com.myapplication.ui.gameplay.glview.utility;

public class GeometryUtils {

    private static double distance = 50.0;

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


}
