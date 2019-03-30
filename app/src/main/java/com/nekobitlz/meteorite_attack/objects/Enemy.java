package com.nekobitlz.meteorite_attack.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.enums.Direction;
import com.nekobitlz.meteorite_attack.options.SoundPlayer;

import java.util.Random;

import static com.nekobitlz.meteorite_attack.views.GameView.*;

public class Enemy {

    private Bitmap bitmap;

    private int x;
    private int y;
    private int screenSizeX;
    private int screenSizeY;
    private int maxX;
    private int maxY;

    private Rect collision;
    private SoundPlayer soundPlayer;
    private Direction currentDirection;

    private int enemies[]; //list of enemies sprites
    private int health;
    private int speed;
    private int level;
    private int value; //"value" coins are awarded for killing

    public Enemy(Context context, int screenSizeX, int screenSizeY, SoundPlayer soundPlayer, int level) {
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        this.soundPlayer = soundPlayer;
        this.level = level;

        health = getRandomHealth(level);
        value = health;
        enemies = new int[] { R.drawable.enemy_black_1, R.drawable.enemy_black_2, R.drawable.enemy_black_3 };

        //Set random image of enemy
        Random random = new Random();
        bitmap = BitmapFactory.decodeResource(context.getResources(), enemies[random.nextInt(3)]);
        bitmap = Bitmap.createScaledBitmap(
                bitmap, bitmap.getWidth() * 3 / 5, bitmap.getHeight() * 3 / 5, false);

        speed = random.nextInt(3) + 1;

        maxX = screenSizeX - bitmap.getWidth();
        maxY = screenSizeY - bitmap.getHeight();

        x = random.nextInt(maxX);
        y = -bitmap.getHeight();

        //If enemy reaches the end of the screen, then changes direction
        if (x < maxX)
            currentDirection = Direction.Right;
        else
            currentDirection = Direction.Left;

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    private int getRandomHealth(int level) {
        int minRange = getRange(level - 1) + 1;
        int maxRange = getRange(level);
        int diff = maxRange - minRange;

        Random random = new Random();
        health = minRange + random.nextInt(diff);

        return health;
    }

    //get range of random
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private int getRange(int level) {
        if (level % 10 == 0)
            return level * 10;

        return level % 10 * 5;
    }

    public void update() {
        y += 7 * speed;

        if (x <= 0)
            currentDirection = Direction.Right;
        else if (x >= screenSizeX - bitmap.getWidth())
            currentDirection = Direction.Left;

        if (currentDirection == Direction.Right)
            x += 7 * speed;
        else
            x -= 7 * speed;

        collision.left = x;
        collision.top = y;
        collision.right = x + bitmap.getWidth();
        collision.bottom = y + bitmap.getHeight();
    }

    public Rect getCollision() {
        return collision;
    }

    //Captures a hit on enemy and if the hit is decisive, it kills the enemy
    public void hit() {
        health -= WEAPON_POWER;

        if (health == 0) {
            SCORE += level * 20;
            ENEMY_DESTROYED++;
            MONEY += value;
            destroy();
        } else {
            soundPlayer.playExplode();
        }
    }

    public void destroy() {
        y = screenSizeY + 1;
        soundPlayer.playCrash();
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
