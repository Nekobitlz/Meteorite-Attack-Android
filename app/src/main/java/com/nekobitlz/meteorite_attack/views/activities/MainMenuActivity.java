package com.nekobitlz.meteorite_attack.views.activities;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.views.fragments.MainMenuFragment;

import java.lang.ref.WeakReference;

public class MainMenuActivity extends AppCompatActivity {

    public static BackgroundSound backgroundSound;
    private boolean isStopped;
    private boolean toOtherActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Make the display full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Make the display always turn on if the activity is active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        backgroundSound = new BackgroundSound(getApplicationContext());
        backgroundSound.execute();

        MainMenuFragment fragment = MainMenuFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.content, fragment)
                .commit();

        isStopped = false;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isStopped) {
            isStopped = false;

            if (backgroundSound != null) {
                backgroundSound.getPlayer().start();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (!toOtherActivity) {
            isStopped = true;

            if (backgroundSound != null && backgroundSound.getPlayer() != null) {
                backgroundSound.getPlayer().pause();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundSound.releaseMP();
    }

    public void setToOtherActivity(boolean toOtherActivity) {
        this.toOtherActivity = toOtherActivity;
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
                    //TODO - fix error with pause on Logcat when sound is off
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

        public MediaPlayer getPlayer() {
            return player;
        }
    }
}
