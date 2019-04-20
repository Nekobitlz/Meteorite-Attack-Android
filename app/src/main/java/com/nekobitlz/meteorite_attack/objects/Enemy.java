package com.nekobitlz.meteorite_attack.objects;

import android.content.Context;
import com.nekobitlz.meteorite_attack.options.SoundPlayer;

public abstract class Enemy {

    Context context;
    int screenSizeX;
    int screenSizeY;
    SoundPlayer soundPlayer;
    int level;

    Enemy(Context context, int screenSizeX, int screenSizeY, SoundPlayer soundPlayer, int level) {
        this.context = context;
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        this.soundPlayer = soundPlayer;
        this.level = level;
    }

    abstract void update();
    abstract void hit();
    abstract void destroy();
}
