package com.nekobitlz.meteorite_attack.views.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.services.MusicService;
import com.nekobitlz.meteorite_attack.views.fragments.MainMenuFragment;

public class MainMenuActivity extends AppCompatActivity {

    private boolean toOtherActivity;

    private boolean isBound = false;
    private MusicService musicService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.ServiceBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Make the display full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Make the display always turn on if the activity is active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        doBindService();
        startService();

        MainMenuFragment fragment = MainMenuFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.content, fragment)
                .commit();
    }

    private void doBindService() {
        bindService(new Intent(this, MusicService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);

        isBound = true;
    }

    private void startService() {
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);
    }

    private void doUnbindService() {
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (musicService != null && musicService.isEnabled() && !toOtherActivity) musicService.resumeMusic();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (musicService != null && !toOtherActivity && !musicService.isEnabled()) musicService.pauseMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicService.stopMusic();
        doUnbindService();
    }

    public void setToOtherActivity(boolean toOtherActivity) {
        this.toOtherActivity = toOtherActivity;
    }

    public MusicService getMusicService() {
        return musicService;
    }
}
