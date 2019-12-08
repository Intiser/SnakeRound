package project.ahsan.language.com.myapplication.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.ahsan.language.com.myapplication.R;
import project.ahsan.language.com.myapplication.ui.gameplay.GamePlayActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.playbutton)
    RelativeLayout playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.playbutton)
    void onPlayButtonClicked(){
        goToGamePlayActivity();
    }

    void goToGamePlayActivity(){
        Intent intent = new Intent(HomeActivity.this, GamePlayActivity.class);
        startActivity(intent);
    }

}
