package project.ahsan.language.com.myapplication.ui.gameplay;

import android.content.Context;
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

    private JoystickView joystickView;
    private CustomView customView;

    private Context context;

    public PlayController(JoystickView joystickView, CustomView customView, Context context) {
        this.joystickView = joystickView;
        this.customView = customView;
        this.context = context;
        init();
        initJoystickView();
    }

    private void init() {
        deviceWidth = context.getResources().getDisplayMetrics().widthPixels;

        deviceHeight = context.getResources().getDisplayMetrics().heightPixels - ViewUtils.getPixelsFromDP(context, 16);
        nowX = deviceWidth / 2;
        nowY = deviceHeight / 2;
        foodPoint = new Point(deviceWidth/4, deviceHeight/4);
        radius = 50.0;
        updateFoodPointAndRadius(foodPoint, radius);

    }


    private void initJoystickView() {

        joystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                Log.d("TAG", "onMove2: " + (angle + 90) % 360 + " degree " + strength);
                nowX = nowX + GeometryUtils.getXcaculated((angle + 90) % 360, strength);
                nowY = nowY + GeometryUtils.getYcaculated((angle + 90) % 360, strength);
                if (nowX < 50) nowX = radius;
                if (nowY < 50) nowY = radius;
                if (nowX > deviceWidth - radius) nowX = deviceWidth - radius;
                if (nowY > deviceHeight - radius) nowY = deviceHeight - radius;

                Log.d("TAG", "onMove2: " + nowX + " nowY " + nowY);
                //relativeLayout.setX((float) nowX);
                //relativeLayout.setY((float) nowY);
                double dis = GeometryUtils.getDistance(foodPoint.getX(),foodPoint.getY(), nowX, nowY);

                if(dis < 20){
                    foodPoint = makenewPoint();
                    radius = radius + 10;
                    updateFoodPointAndRadius(foodPoint, radius);
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

    private void updateFoodPointAndRadius(Point point,double radius){
        customView.queueEvent(new Runnable() {
            @Override
            public void run() {
                customView.setFoodPointsToRenderer(point);
                customView.setRadiusOfFood(radius);
            }
        });
    }

    private Point makenewPoint(){
        double x =  ( (Math.random()%deviceWidth) * deviceWidth);
        double y =  ( (Math.random()%deviceHeight) * deviceHeight);
        Point point = new Point(x,y);
        return point;
    }

}
