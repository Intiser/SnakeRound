package project.ahsan.language.com.myapplication.ui.gameplay;

import androidx.appcompat.app.AppCompatActivity;
import io.github.controlwear.virtual.joystick.android.JoystickView;
import project.ahsan.language.com.myapplication.R;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.model.Point;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.utility.GeometryUtils;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.utility.ViewUtils;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.CustomView;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

public class GamePlayActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    RelativeLayout motherLayout;




    JoystickView joystickView;
    CustomView customView;

    double nowX = 480.0;
    double nowY = 912.0;

    double deviceWidth;
    double deviceHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initLayouts();

        //initIndicators();
        initJoystickView();
        initGLView();

    }

    void initGLView(){
        customView = findViewById(R.id.customview);

    }

    private void initJoystickView() {
        joystickView = findViewById(R.id.joystick);
        joystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                Log.d("TAG", "onMove2: " + (angle+ 90 )%360+ " degree " + strength);
                nowX = nowX + GeometryUtils.getXcaculated((angle + 90)%360, strength);
                nowY = nowY + GeometryUtils.getYcaculated((angle + 90)%360, strength);
                if(nowX < 50) nowX = 50;
                if(nowY < 50) nowY = 50;
                if(nowX > deviceWidth-50) nowX =  deviceWidth-50;
                if(nowY > deviceHeight-50) nowY =  deviceHeight-50;

                Log.d("TAG", "onMove2: " + nowX + " nowY " + nowY);
                //relativeLayout.setX((float) nowX);
                //relativeLayout.setY((float) nowY);

                customView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        customView.setPointToRenderer(new Point(nowX,nowY));
                    }
                });
            }
        });
    }



    private void initLayouts() {
        relativeLayout = findViewById(R.id.layout);
        motherLayout = findViewById(R.id.mother_layout);
        //nowX = relativeLayout.getX();
        //nowY = relativeLayout.getY();
        nowX = 480.0;
        nowY = 912.0;
        deviceWidth = this.getResources().getDisplayMetrics().widthPixels;
        deviceHeight = this.getResources().getDisplayMetrics().heightPixels - ViewUtils.getPixelsFromDP(this, 10);
    }
}
