package com.nekobitlz.meteorite_attack.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button play, highScore, exit; //TODO(): Shop
    private TextView money;
    private LinearLayout moneyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Make the display full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Make the display always turn on if the activity is active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        play = findViewById(R.id.play);
        highScore = findViewById(R.id.high_score);
        exit = findViewById(R.id.exit);
        moneyContainer = findViewById(R.id.money_container);
        money = findViewById(R.id.money);

        play.setOnClickListener(this);
        highScore.setOnClickListener(this);
        exit.setOnClickListener(this);

        loadMoney();
    }

    @SuppressLint("SetTextI18n")
    private void loadMoney() {
        SharedPreferencesManager spm = new SharedPreferencesManager(this);
        money.setText(spm.getMoney() + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

            case R.id.high_score:
                startActivity(new Intent(this, HighScoreActivity.class));
                break;

            case R.id.exit:
                finish();
                break;
        }
    }
}
