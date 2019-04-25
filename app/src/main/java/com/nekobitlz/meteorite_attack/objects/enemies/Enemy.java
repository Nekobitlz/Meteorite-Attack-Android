package com.nekobitlz.meteorite_attack.objects.enemies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import com.nekobitlz.meteorite_attack.enums.EnemyType;
import com.nekobitlz.meteorite_attack.options.SoundPlayer;

public abstract class Enemy {

    public Context context;
    public int screenSizeX;
    public int screenSizeY;
    public SoundPlayer soundPlayer;
    public int level;
    public int alpha;

    public Enemy(Context context, int screenSizeX, int screenSizeY, SoundPlayer soundPlayer, int level) {
        this.context = context;
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        this.soundPlayer = soundPlayer;
        this.level = level;
        alpha = 255;
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
    public abstract int getAlpha();
    public abstract void setAlpha(int alpha);
}
