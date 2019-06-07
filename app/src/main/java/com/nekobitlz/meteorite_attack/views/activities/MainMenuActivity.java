package com.nekobitlz.meteorite_attack.views.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;

import java.lang.ref.WeakReference;

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
    private boolean isStopped;
    public static BackgroundSound backgroundSound;
    private boolean toOtherActivity;

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

        backgroundSound = new BackgroundSound(getApplicationContext());
        backgroundSound.execute();
        isStopped = false;
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
                toOtherActivity = true;
            }
            break;

            case R.id.shop: {
                startActivity(new Intent(this, ShopActivity.class));
                toOtherActivity = true;
            }
            break;

            case R.id.high_score: {
                startActivity(new Intent(this, HighScoreActivity.class));
                toOtherActivity = true;
            }
            break;

            case R.id.settings: {
                startActivity(new Intent(this, SettingsActivity.class));
                toOtherActivity = true;
            }
            break;

            case R.id.exit: {
                if (backgroundSound != null) {
                    backgroundSound.releaseMP();
                }

                finish();
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMoney();

        if (toOtherActivity) {
            toOtherActivity = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isStopped) {
            isStopped = false;

            if (backgroundSound != null) {
                backgroundSound.player.start();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!toOtherActivity) {
            isStopped = true;

            if (backgroundSound != null) {
                backgroundSound.player.pause();
            }
        }
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

    /*
        AsyncTask for background music
    */
    public static class BackgroundSound extends AsyncTask<Void, Void, Void> {

        private WeakReference<Context> weakContext;
        private SharedPreferencesManager spm;
        private MediaPlayer player;
        private float volume;
        private boolean isEnabled;
        private boolean isPlayerPlaying;

        public BackgroundSound(Context context) {
            weakContext = new WeakReference<>(context);
            spm = new SharedPreferencesManager(weakContext.get());
            volume = spm.getMusicVolume() / 100f;
            isEnabled = spm.getSoundStatus();
            isPlayerPlaying = true;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            player = MediaPlayer.create(weakContext.get(), R.raw.main_music);
            player.setLooping(true);
            player.start();

            while (!isCancelled()) {
                if (isEnabled) {
                    if (isPlayerPlaying) {
                        player.setVolume(volume, volume);
                    } else {
                        player.start();
                        isPlayerPlaying = true;
                    }
                } else {
                    player.pause();
                    isPlayerPlaying = false;
                }
            }

            releaseMP();

            return null;
        }

        public void setVolume(int volume) {
            this.volume = volume / 100f;
        }

        public void setEnabled(boolean enabled) {
            isEnabled = enabled;
        }

        public void releaseMP() {
            if (player != null) {
                try {
                    player.stop();
                    player.release();
                    player = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
