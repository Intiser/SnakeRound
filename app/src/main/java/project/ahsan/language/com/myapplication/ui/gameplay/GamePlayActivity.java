package project.ahsan.language.com.myapplication.ui.gameplay;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
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


    @BindView(R.id.joystick)
    JoystickView joystickView;

    @BindView(R.id.customview)
    CustomView customView;

    PlayController playController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        initController();
    }

    private void initController() {
        playController = new PlayController(joystickView, customView, this);
    }


}
