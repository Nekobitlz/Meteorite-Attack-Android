package com.nekobitlz.meteorite_attack.options;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.enums.GameStatus;

public class SoundPlayer implements Runnable {

    private Thread soundThread;
    private volatile GameStatus currentStatus = GameStatus.Paused;

    private SoundPool soundPool;
    private int explodeId, laserId, crashId;
    private boolean isLaserPlaying, isExplodePlaying, isCrashPlaying;

    @SuppressWarnings("deprecation")
    public SoundPlayer(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes
                    .Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool
                    .Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(attributes)
                    .build();
        } else
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);

        explodeId = soundPool.load(context, R.raw.rock_explode_1, 1);
        laserId = soundPool.load(context, R.raw.laser_1, 1);
        crashId = soundPool.load(context, R.raw.rock_explode_2, 1);
    }

    @Override
    public void run() {
        while (currentStatus == GameStatus.Playing) {
            if (isLaserPlaying) {
                soundPool.play(laserId, 1, 1, 1, 0, 1);
                isLaserPlaying = false;
            }

            if (isExplodePlaying) {
                soundPool.play(explodeId, 1, 1, 1, 0, 1);
                isExplodePlaying = false;
            }

            if (isCrashPlaying) {
                soundPool.play(crashId, 0.7f, 0.7f, 1, 0, 1);
                isCrashPlaying = false;
            }
        }
    }

    public void playCrash() {
        isCrashPlaying = true;
    }

    public void playLaser() {
        isLaserPlaying = true;
    }

    public void playExplode() {
        isExplodePlaying = true;
    }

    public void resume() {
        currentStatus = GameStatus.Playing;

        soundThread = new Thread(this);
        soundThread.start();
    }

    public void pause() throws InterruptedException {
        currentStatus = GameStatus.Paused;

        soundThread.join();
    }
}
