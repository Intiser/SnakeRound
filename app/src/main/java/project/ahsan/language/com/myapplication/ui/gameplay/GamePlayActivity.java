package project.ahsan.language.com.myapplication.ui.gameplay;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.controlwear.virtual.joystick.android.JoystickView;
import project.ahsan.language.com.myapplication.R;
import project.ahsan.language.com.myapplication.ui.gameplay.controler.PlayController;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.CustomView;

import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initController() {
        playController = new PlayController(joystickView, customView, this);
    }

    private void makeFullscreen() {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.

    }


}
