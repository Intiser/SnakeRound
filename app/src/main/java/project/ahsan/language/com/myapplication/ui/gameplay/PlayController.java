package project.ahsan.language.com.myapplication.ui.gameplay;

import android.content.Context;
import android.util.Log;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.CustomView;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.model.Point;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.utility.GeometryUtils;

public class PlayController {

    private double nowX;
    private double nowY;

    private double deviceWidth;
    private double deviceHeight;

    Point foodPoint;

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

        deviceHeight = context.getResources().getDisplayMetrics().heightPixels;
        nowX = deviceWidth / 2;
        nowY = deviceHeight / 2;
        foodPoint = new Point(deviceWidth/4, deviceHeight/4);
        updateFoodPoint(foodPoint);
    }


    private void initJoystickView() {

        joystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                Log.d("TAG", "onMove2: " + (angle + 90) % 360 + " degree " + strength);
                nowX = nowX + GeometryUtils.getXcaculated((angle + 90) % 360, strength);
                nowY = nowY + GeometryUtils.getYcaculated((angle + 90) % 360, strength);
                if (nowX < 50) nowX = 50;
                if (nowY < 50) nowY = 50;
                if (nowX > deviceWidth - 50) nowX = deviceWidth - 50;
                if (nowY > deviceHeight - 50) nowY = deviceHeight - 50;

                Log.d("TAG", "onMove2: " + nowX + " nowY " + nowY);
                //relativeLayout.setX((float) nowX);
                //relativeLayout.setY((float) nowY);
                double dis = GeometryUtils.getDistance(foodPoint.getX(),foodPoint.getY(), nowX, nowY);

                if(dis < 20){
                    foodPoint = makenewPoint();
                    updateFoodPoint(foodPoint);
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

    private void updateFoodPoint(Point point){
        customView.queueEvent(new Runnable() {
            @Override
            public void run() {
                customView.setFoodPointsToRenderer(point);
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
