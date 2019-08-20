package com.nekobitlz.meteorite_attack.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;

public class MusicService extends Service implements MediaPlayer.OnErrorListener {

    private final IBinder binder = new ServiceBinder();
    private SharedPreferencesManager spm;
    private MediaPlayer player;

    private int length = 0;
    private float volume;
    private boolean isEnabled;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("M_MusicService", "onCreate");

        player = MediaPlayer.create(this, R.raw.main_music);
        player.setOnErrorListener(this);

        spm = new SharedPreferencesManager(this);
        volume = spm.getMusicVolume() / 100f;
        isEnabled = spm.getSoundStatus();

        if (player != null) {
            player.setLooping(true);
            player.setVolume(volume, volume);
        }

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                onError(player, what, extra);
                return true;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();

        Log.d("M_MusicService", "onStartCommand");
        return START_STICKY;
    }

    public void pauseMusic() {
        if (player != null && player.isPlaying()) {
            player.pause();
            length = player.getCurrentPosition();
        }

        Log.d("M_MusicService", "pauseMusic");
    }

    public void resumeMusic() {
        if (player != null && !player.isPlaying() && isEnabled) {
            player.seekTo(length);
            player.start();
        }

        Log.d("M_MusicService", "resumeMusic");
    }

    public void stopMusic() {
        player.stop();
        player.release();
        player = null;

        Log.d("M_MusicService", "stopMusic");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (player != null) {
            try {
                player.stop();
                player.release();
            } finally {
                player = null;
            }
        }

        Log.d("M_MusicService", "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("M_MusicService", "onBind");
        return binder;
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        Toast.makeText(this, "Music player failed", Toast.LENGTH_SHORT).show();
        Log.d("M_MusicService", "onError");

        if (player != null) {
            try {
                player.stop();
                player.release();
            } finally {
                player = null;
            }
        }

        return false;
    }

    public void setVolume(int progress) {
        this.volume = progress / 100f;

        resumeMusic();
        player.setVolume(volume, volume);
    }

    public void setEnabled(boolean isEnabled) {
        if (isEnabled) {
            resumeMusic();
            player.setVolume(volume, volume);
        } else {
            pauseMusic();
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public class ServiceBinder extends Binder {

        public MusicService getService() {
            return MusicService.this;
        }
    }
}