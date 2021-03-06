package com.nekobitlz.meteorite_attack.options;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.enums.GameStatus;

/*
    Sounds playback
*/
public class SoundPlayer implements Runnable {

    private Thread soundThread;
    private volatile GameStatus currentStatus = GameStatus.Paused;

    private SoundPool soundPool;
    private int explodeId;
    private int laserId;
    private int crashId;

    private boolean isLaserPlaying;
    private boolean isExplodePlaying;
    private boolean isCrashPlaying;

    private float volume;
    private boolean isEnabled;
    private SharedPreferencesManager spm;

    /*
        Sounds initialization
    */
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

        spm = new SharedPreferencesManager(context);
        volume = spm.getEffectsVolume() / 100f;
        isEnabled = spm.getSoundStatus();
    }

    /*
        Playback
    */
    @Override
    public void run() {
        if (isEnabled) {
            while (currentStatus == GameStatus.Playing) {
                if (isLaserPlaying) {
                    soundPool.play(laserId, volume, volume, 1, 0, 1);
                    isLaserPlaying = false;
                }

                if (isExplodePlaying) {
                    soundPool.play(explodeId, volume, volume, 1, 0, 1);
                    isExplodePlaying = false;
                }

                if (isCrashPlaying) {
                    soundPool.play(crashId, volume, volume, 1, 0, 1);
                    isCrashPlaying = false;
                }
            }
        }
    }

    /*
        Sets crashPlaying state for playback
    */
    public void playCrash() {
        isCrashPlaying = true;
    }

    /*
        Sets laserPlaying state for playback
    */
    public void playLaser() {
        isLaserPlaying = true;
    }

    /*
        Sets explodePlaying state for playback
    */
    public void playExplode() {
        isExplodePlaying = true;
    }

    /*
        Resumes sound playback after pause
    */
    public void resume() {
        currentStatus = GameStatus.Playing;

        soundThread = new Thread(this);
        soundThread.start();
    }

    /*
        Pauses sound playback
    */
    public void pause() throws InterruptedException {
        currentStatus = GameStatus.Paused;

        soundThread.join();
    }

    /*
        Removes all loaded sounds from SoundPool and frees up memory
    */
    public void release() {
        soundPool.release();
    }
}