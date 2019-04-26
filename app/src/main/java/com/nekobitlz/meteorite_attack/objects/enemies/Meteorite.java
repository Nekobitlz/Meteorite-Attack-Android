package com.nekobitlz.meteorite_attack.objects.enemies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.enums.EnemyType;
import com.nekobitlz.meteorite_attack.options.SoundPlayer;

import java.util.Random;

import static com.nekobitlz.meteorite_attack.views.GameView.*;

/*
    Hostile flying meteorite
*/
public class Meteorite extends Enemy {

    private Bitmap bitmap;
    private Rect collision;

    private int x;
    private int y;
    private int maxX;

    private int meteors[];
    private int speed;
    private int health;
    private int value; //"value" coins are awarded for killing
    private int size;
    private boolean isDestroyed;

    /*
        Meteorite initialization
    */
    public Meteorite(Context context, int screenSizeX, int screenSizeY, SoundPlayer soundPlayer, int level) {
        super(context, screenSizeX, screenSizeY, soundPlayer, level);

        isDestroyed = false;
        health = getRandomHealth(level);
        value = health;
        meteors = new int[] { R.drawable.meteor_1, R.drawable.meteor_2, R.drawable.meteor_3, R.drawable.meteor_4 };

        //Set random meteor image
        Random random = new Random();

        size = random.nextInt(5) + 2;
        bitmap = BitmapFactory.decodeResource(context.getResources(), meteors[random.nextInt(4)]);
        bitmap = Bitmap.createScaledBitmap(
                bitmap, bitmap.getWidth() * size / 5, bitmap.getHeight() * size / 5, false);

        //Set random speed
        speed = random.nextInt(3) + 1;

        maxX = screenSizeX - bitmap.getWidth();

        x = random.nextInt(maxX);
        y = -bitmap.getHeight();

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    /*
       Gets a random amount of health for an enemy in a given range
    */
    private int getRandomHealth(int level) {
        Random random = new Random();
        health = level + random.nextInt(level * 2);

        return health;
    }

    /*
        Updates meteorite state
    */
    @Override
    public void update() {
        y += speed * 7;

        if (!isDestroyed) {
            collision.left = x;
            collision.top = y;
            collision.right = x + bitmap.getWidth();
            collision.bottom = y + bitmap.getHeight();
        }
    }

    /*
        Captures a hit on meteorite and if the hit is decisive, it kills the meteorite
    */
    @Override
    public void hit() {
        health -= WEAPON_POWER;

        if (health <= 0) {
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

        Removes collision rect and sets explosion
    */
    @Override
    public void destroy() {
        isDestroyed = true;
        speed = 1;

        collision.set(0, 0, 0, 0);
        Bitmap explodeBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);
        bitmap = Bitmap.createScaledBitmap(
                explodeBitmap, bitmap.getWidth(), bitmap.getHeight(), false);

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

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public EnemyType getEnemyType() {
        return EnemyType.Meteorite;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}
