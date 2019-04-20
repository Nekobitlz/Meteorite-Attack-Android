package com.nekobitlz.meteorite_attack.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;

/*
    Activity with main menu
*/
public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button play;
    private Button exit;
    private ImageButton shop;
    private ImageButton highScore;
    private ImageButton settings;

    private TextView money;
    private SharedPreferencesManager spm;
    private long backPressed;

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
        money = findViewById(R.id.money);
        shop = findViewById(R.id.shop);
        settings = findViewById(R.id.settings);

        play.setOnClickListener(this);
        shop.setOnClickListener(this);
        highScore.setOnClickListener(this);
        exit.setOnClickListener(this);
        settings.setOnClickListener(this);

        spm = new SharedPreferencesManager(this);
        loadMoney();
    }

    /*
        Loads your saved money
    */
    @SuppressLint("SetTextI18n")
    public void loadMoney() {
        money.setText(spm.getMoney() + "");
    }

    /*
        Reads views clicks
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play: {
                startActivity(new Intent(this, MainActivity.class));
            }
            break;

            case R.id.shop: {
                startActivity(new Intent(this, ShopActivity.class));
            }
            break;

            case R.id.high_score: {
                startActivity(new Intent(this, HighScoreActivity.class));
            }
            break;

            case R.id.settings: {
                startActivity(new Intent(this, SettingsActivity.class));
            }
            break;

            case R.id.exit: {
                finish();
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadMoney();
    }

    /*
        Exit from the application by double clicking on the Back button
    */
    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();

        backPressed = System.currentTimeMillis();
    }
}
