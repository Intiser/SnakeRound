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


    JoystickView joystickView;
    CustomView customView;

    PlayController playController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();
        initController();
    }

    private void init(){
        customView = findViewById(R.id.customview);
        joystickView = findViewById(R.id.joystick);
    }

    private void initController(){
        playController = new PlayController(joystickView, customView, this);
    }


}
