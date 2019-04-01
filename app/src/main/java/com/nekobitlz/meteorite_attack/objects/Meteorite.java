package com.nekobitlz.meteorite_attack.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SoundPlayer;

import java.util.Random;

import static com.nekobitlz.meteorite_attack.views.GameView.*;

/*
    Hostile flying meteorite
*/
public class Meteorite {

    private Bitmap bitmap;

    private int x;
    private int y;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private int screenSizeX;
    private int screenSizeY;

    private int meteors[];
    private int speed;
    private int health;
    private int level;
    private int value; //"value" coins are awarded for killing

    private Rect collision;
    private SoundPlayer soundPlayer;

    /*
        Meteorite initialization
    */
    public Meteorite(Context context, int screenSizeX, int screenSizeY, SoundPlayer soundPlayer, int level) {
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        this.soundPlayer = soundPlayer;
        this.level = level;

        value = health;
        health = getRandomHealth(level);
        meteors = new int[] { R.drawable.meteor_1, R.drawable.meteor_2, R.drawable.meteor_3, R.drawable.meteor_4 };

        //Set random meteor image
        Random random = new Random();
        bitmap = BitmapFactory.decodeResource(context.getResources(), meteors[random.nextInt(4)]);
        bitmap = Bitmap.createScaledBitmap(
                bitmap, bitmap.getWidth() * 3 / 5, bitmap.getHeight() * 3 / 5, false);

        //Set random speed
        speed = random.nextInt(3) + 1;

        maxX = screenSizeX - bitmap.getWidth();
        maxY = screenSizeY - bitmap.getHeight();
        minX = 0;
        minY = 0;

        x = random.nextInt(maxX);
        y = -bitmap.getHeight();

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    /*
       Gets a random amount of health for an enemy in a given range
    */
    private int getRandomHealth(int level) {
        int minRange = getRange(level - 1) + 1;
        int maxRange = getRange(level);
        int diff = maxRange - minRange;

        Random random = new Random();
        health = minRange + random.nextInt(diff);

        return health;
    }

    /*
        Gets range of random
    */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private int getRange(int level) {
        if (level % 10 == 0)
            return level * 10;

        return level % 10 * 5;
    }

    /*
        Updates meteorite state
    */
    public void update() {
        y += speed * 7;

        collision.left = x;
        collision.top = y;
        collision.right = x + bitmap.getWidth();
        collision.bottom = y + bitmap.getHeight();
    }

    /*
        Captures a hit on meteorite and if the hit is decisive, it kills the meteorite
    */
    public void hit() {
        health -= WEAPON_POWER;

        if (health == 0) {
            SCORE += level * 10;
            METEOR_DESTROYED++;
            MONEY += value;
            destroy();
        } else {
            soundPlayer.playExplode();
        }
    }

    /*
        Destroys meteorite
    */
    public void destroy() {
        y = screenSizeY + 1;
        soundPlayer.playCrash();
    }

    /*
        GETTERS
    */
    public Rect getCollision() {
        return collision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }
}
