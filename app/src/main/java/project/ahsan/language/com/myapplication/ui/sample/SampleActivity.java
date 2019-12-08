package project.ahsan.language.com.myapplication.ui.sample;

import androidx.appcompat.app.AppCompatActivity;
import io.github.controlwear.virtual.joystick.android.JoystickView;
import project.ahsan.language.com.myapplication.R;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.utility.GeometryUtils;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

public class SampleActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    RelativeLayout motherLayout;




    JoystickView joystickView;

    double nowX = 480.0;
    double nowY = 912.0;

    double changePos = 5.0;
    double changeNeg = -5.0;
    double notchange = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayouts();

        //initIndicators();
        initJoystickView();

    }

    private void initJoystickView() {
       joystickView = findViewById(R.id.joystick);
       joystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
           @Override
           public void onMove(int angle, int strength) {
               Log.d("TAG", "onMove: " + (angle+ 90 )%360+ " degree " + strength);
               nowX = nowX + GeometryUtils.getXcaculated((angle + 90)%360, strength);
               nowY = nowY + GeometryUtils.getYcaculated((angle + 90)%360, strength);
               Log.d("TAG", "onMove: " + nowX + " nowY " + nowY);
               relativeLayout.setX((float) nowX);
               relativeLayout.setY((float) nowY);
           }
       });
    }



    private void initLayouts() {
        relativeLayout = findViewById(R.id.layout);
        motherLayout = findViewById(R.id.mother_layout);
        nowX = relativeLayout.getX();
        nowY = relativeLayout.getY();
        nowX = 480.0;
        nowY = 912.0;
    }




}
