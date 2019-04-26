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
    Hostile flying "border destroyer" meteorite
*/
public class BorderDestroyerMeteor extends Enemy {

    private Bitmap bitmap;
    private Rect collision;

    private int x;
    private int y;
    private int maxX;

    private int speed;
    private int health;
    private int value; //"value" coins are awarded for killing
    private boolean isDestroyed;

    public BorderDestroyerMeteor(Context context, int screenSizeX, int screenSizeY, SoundPlayer soundPlayer, int level) {
        super(context, screenSizeX, screenSizeY, soundPlayer, level);

        isDestroyed = false;
        health = getRandomHealth(level);
        value = health;

        //Set random "border destroyer" meteor image
        Random random = new Random();
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.meteor_brown_big_1);
        bitmap = Bitmap.createScaledBitmap(
                bitmap, bitmap.getWidth() * 3 / 5, bitmap.getHeight() * 3 / 5, false);

        //Set random speed
        speed = 2;

        maxX = screenSizeX - bitmap.getWidth();

        x = random.nextInt(maxX);
        y = -bitmap.getHeight();

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    /*
       Gets a random amount of health for an "border destroyer" in a given range
    */
    private int getRandomHealth(int level) {
        Random random = new Random();
        health = level + random.nextInt(level);

        return health;
    }

    /*
        Updates "border destroyer" meteorite state
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
        Captures a hit on "border destroyer" meteorite
        and if the hit is decisive, it kills the "border destroyer" meteorite
    */
    @Override
    public void hit() {
        health -= WEAPON_POWER;

        if (health <= 0) {
            SCORE += level * 15;
            METEOR_DESTROYED++;
            MONEY += value * 1.5;
            destroy();
        } else {
            soundPlayer.playExplode();
        }
    }

    /*
        Destroys "border destroyer" meteorite

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
        return EnemyType.BorderDestroyer;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}
