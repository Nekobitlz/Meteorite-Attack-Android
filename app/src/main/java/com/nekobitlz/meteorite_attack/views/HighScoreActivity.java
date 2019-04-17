package com.nekobitlz.meteorite_attack.views;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;

/*
   Activity that show your high score
*/
public class HighScoreActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;

    private TextView score;
    private TextView meteor;
    private TextView enemy;
    private TextView nullHighScore;

    private LinearLayout highScoreContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        //Make the display full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Make the display always turn on if the activity is active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        back = findViewById(R.id.back);
        score = findViewById(R.id.score);
        meteor = findViewById(R.id.meteor);
        enemy = findViewById(R.id.enemyShip);
        nullHighScore = findViewById(R.id.null_high_score);
        highScoreContainer = findViewById(R.id.high_score_container);

        back.setOnClickListener(this);
        loadHighScore();
    }

    /*
        Loads your saved high score
    */
    @SuppressLint("SetTextI18n")
    private void loadHighScore() {
        SharedPreferencesManager spm = new SharedPreferencesManager(this);

        if (spm.getHighScore() != -1) {
            nullHighScore.setVisibility(TextView.GONE);
            highScoreContainer.setVisibility(LinearLayout.VISIBLE);

            score.setText(spm.getHighScore() + "");
            meteor.setText(spm.getMeteorDestroyed() + "");
            enemy.setText(spm.getEnemyDestroyed() + "");
        } else {
            nullHighScore.setVisibility(TextView.VISIBLE);
            highScoreContainer.setVisibility(LinearLayout.GONE);
        }
    }

    /*
        Reads views clicks
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}