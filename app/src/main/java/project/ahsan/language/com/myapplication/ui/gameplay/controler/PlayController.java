package project.ahsan.language.com.myapplication.ui.gameplay.controler;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.CustomView;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.model.Point;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.utility.GeometryUtils;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.utility.ViewUtils;

public class PlayController {

    private double nowX;
    private double nowY;

    private double deviceWidth;
    private double deviceHeight;

    Point foodPoint;
    double radius;

    double difference;
    double border;

    private JoystickView joystickView;
    private CustomView customView;

    private Context context;

    Vibrator vibrator;

    public PlayController(JoystickView joystickView, CustomView customView, Context context) {
        this.joystickView = joystickView;
        this.customView = customView;
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        init();
        initJoystickView();
    }

    private void init() {


        deviceWidth = context.getResources().getDisplayMetrics().widthPixels;
        deviceHeight = context.getResources().getDisplayMetrics().heightPixels - ViewUtils.getPixelsFromDP(context, 16);
        nowX = deviceWidth / 2;
        nowY = deviceHeight / 2;
        border = 10.0;
        radius = 50.0;
        difference = 20.0;
        foodPoint = makenewPoint();
        updateFoodPointAndRadius(foodPoint, radius);
        customView.queueEvent(new Runnable() {
            @Override
            public void run() {
                customView.setBorder(border);
            }
        });

    }


    private void initJoystickView() {

        joystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                Log.d("TAG", "onMove2: " + (angle + 90) % 360 + " degree " + strength);
                nowX = nowX + GeometryUtils.getXcaculated((angle + 90) % 360, strength);
                nowY = nowY + GeometryUtils.getYcaculated((angle + 90) % 360, strength);
                double radbod = (radius + border);
                if (nowX < radbod) nowX = radbod;
                if (nowY < radbod) nowY = radbod;
                if (nowX > deviceWidth - radbod) nowX = deviceWidth - radbod;
                if (nowY > deviceHeight - radbod) nowY = deviceHeight - radbod;

                Log.d("TAG", "onMove2: " + nowX + " nowY " + nowY);
                //relativeLayout.setX((float) nowX);
                //relativeLayout.setY((float) nowY);
                double dis = GeometryUtils.getDistance(foodPoint.getX(), foodPoint.getY(), nowX, nowY);

                if (dis < difference) {

                    gotRewardPoint();

                }


                customView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        customView.setPointsToRenderer(new Point(nowX, nowY));
                    }
                });


            }
        });
    }

    void gotRewardPoint(){
        radius = radius + 10;
        foodPoint = makenewPoint();
        updateFoodPointAndRadius(foodPoint, radius);
        makeVibration();
    }

    private void makeVibration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.EFFECT_HEAVY_CLICK));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }

    private void updateFoodPointAndRadius(Point point, double radius) {
        customView.queueEvent(new Runnable() {
            @Override
            public void run() {
                customView.setFoodPointsToRenderer(point);
                customView.setRadiusOfFood(radius);
            }
        });
    }

    private Point makenewPoint() {
        double radbod = radius + border;
        double width = deviceWidth - 2 * radbod;
        double height = deviceHeight - 2 * radbod;
        double x = ((Math.random() % width) * width);
        double y = ((Math.random() % height) * height);
        Point point = new Point(x + radbod, y + radbod);
        return point;
    }

}
