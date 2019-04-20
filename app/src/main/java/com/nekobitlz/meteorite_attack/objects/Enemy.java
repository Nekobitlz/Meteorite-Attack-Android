package com.nekobitlz.meteorite_attack.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import com.nekobitlz.meteorite_attack.enums.EnemyType;
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

    public abstract void update();
    public abstract void hit();
    public abstract void destroy();

    /*
        GETTERS
    */
    public abstract int getX();
    public abstract Rect getCollision();
    public abstract int getY();
    public abstract int getHealth();
    public abstract Bitmap getBitmap();
    public abstract boolean isDestroyed();
    public abstract EnemyType getEnemyType();
}
