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
    Hostile exploder meteorite
*/
public class ExploderMeteor extends Enemy {

    private Bitmap bitmap;
    private Rect collision;

    private int x;
    private int y;
    private int maxX;

    private int speed;
    private int health;
    private int value; //"value" coins are awarded for killing
    private boolean isDestroyed;
    private int alpha;

    /*
        Exploder initialization
    */
    public ExploderMeteor(Context context, int screenSizeX, int screenSizeY, SoundPlayer soundPlayer, int level) {
        super(context, screenSizeX, screenSizeY, soundPlayer, level);

        isDestroyed = false;
        health = getRandomHealth(level);
        value = health;

        //Set random meteor image
        Random random = new Random();
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.meteor_grey_big_3);
        bitmap = Bitmap.createScaledBitmap(
                bitmap, bitmap.getWidth() * 3 / 5, bitmap.getHeight() * 3 / 5, false);

        //Set random speed
        speed = random.nextInt(3) + 1;

        maxX = screenSizeX - bitmap.getWidth();

        x = random.nextInt(maxX);
        y = -bitmap.getHeight();

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    /*
       Gets a random amount of health for an exploder in a given range
    */
    private int getRandomHealth(int level) {
        Random random = new Random();
        health = level + random.nextInt(level * 2);

        return health;
    }

    /*
        Updates exploder state
    */
    @Override
    public void update() {
        y += speed * 7;

        collision.left = x;
        collision.top = y;
        collision.right = x + bitmap.getWidth();
        collision.bottom = y + bitmap.getHeight();
    }

    /*
        Captures a hit on exploder and if the hit is decisive, it kills the exploder
    */
    @Override
    public void hit() {
        health -= WEAPON_POWER;

        if (health <= 0) {
            SCORE += level * 10;
            EXPLODER_DESTROYED++;
            MONEY += value;
            destroy();
        } else {
            soundPlayer.playExplode();
        }
    }

    /*
        Destroys exploder and create explosion

        If meteor destroys first time
                    -> his bitmap (crater) becomes equals to size of meteorite multiplied by 2
    */
    @Override
    public void destroy() {
        speed = 1;

        if (!isDestroyed) {
            x -= bitmap.getWidth() / 2;
            y -= bitmap.getHeight() / 2;

            Bitmap explodeBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_crater);
            bitmap = Bitmap.createScaledBitmap(
                    explodeBitmap, bitmap.getWidth() * 2, bitmap.getHeight() * 2, false);
        }

        isDestroyed = true;
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
        return EnemyType.Exploder;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}
