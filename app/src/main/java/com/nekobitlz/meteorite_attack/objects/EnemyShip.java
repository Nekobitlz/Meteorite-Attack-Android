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

/*
    Enemy flying ship
*/
public class EnemyShip {

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

    private int enemyShips[]; //list of enemy ships sprites
    private int health;
    private int speed;
    private int level;
    private int value; //"value" coins are awarded for killing

    /*
        Enemy ship initialization
    */
    public EnemyShip(Context context, int screenSizeX, int screenSizeY, SoundPlayer soundPlayer, int level) {
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        this.soundPlayer = soundPlayer;
        this.level = level;

        health = getRandomHealth(level);
        value = health;
        enemyShips = new int[] { R.drawable.enemy_black_1, R.drawable.enemy_black_2, R.drawable.enemy_black_3 };

        //Set random image of enemy ship
        Random random = new Random();
        bitmap = BitmapFactory.decodeResource(context.getResources(), enemyShips[random.nextInt(3)]);
        bitmap = Bitmap.createScaledBitmap(
                bitmap, bitmap.getWidth() * 3 / 5, bitmap.getHeight() * 3 / 5, false);

        speed = random.nextInt(3) + 1;

        maxX = screenSizeX - bitmap.getWidth();
        maxY = screenSizeY - bitmap.getHeight();

        x = random.nextInt(maxX);
        y = -bitmap.getHeight();

        //If enemy ship reaches the end of the screen, then changes direction
        if (x < maxX)
            currentDirection = Direction.Right;
        else
            currentDirection = Direction.Left;

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    /*
        Gets a random amount of health for an enemy ship in a given range
    */
    private int getRandomHealth(int level) {
        Random random = new Random();
        health = level + random.nextInt(WEAPON_POWER * 2);

        return health;
    }

    /*
        Updates enemy ship state
    */
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

    /*
        Captures a hit on enemy ship and if the hit is decisive, it kills the enemy ship
    */
    public void hit() {
        health -= WEAPON_POWER;

        if (health <= 0) {
            SCORE += level * 20;
            ENEMY_SHIP_DESTROYED++;
            MONEY += value;
            destroy();
        } else {
            soundPlayer.playExplode();
        }
    }

    /*
        Destroys enemy ship
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
