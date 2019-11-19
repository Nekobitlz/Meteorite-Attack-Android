package com.nekobitlz.meteorite_attack.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.enums.BonusType;
import com.nekobitlz.meteorite_attack.options.SoundPlayer;

import java.util.Random;

/*
    Positive and negative bonuses that player can catch
*/
public class Bonus {

    private BonusType bonusType;
    private int screenSizeY;

    private int x;
    private int y;
    private int speed;
    private int image;

    private Bitmap bitmap;
    private Rect collision;

    /*
        Bonus initialization
    */
    public Bonus(Context context, int screenSizeX, int screenSizeY, BonusType bonusType) {
        this.screenSizeY = screenSizeY;
        this.bonusType = bonusType;

        speed = 1;

        switch (bonusType) {
            case Health: {
                image = R.drawable.bonus_health;
            }
            break;
            case Speed_up: {
                image = R.drawable.bonus_speed_up;
            }
            break;
            case Triple_shot: {
                image = R.drawable.bonus_triple_shot;
            }
            break;
            case Slow_up: {
                image = R.drawable.bonus_slow_up;
            }
            break;
            case Destroyer: {
                image = R.drawable.bonus_destroyer;
            }
            break;
        }

        bitmap = BitmapFactory.decodeResource(context.getResources(), image);
        bitmap = Bitmap.createScaledBitmap(
                bitmap,bitmap.getWidth() / 20, bitmap.getHeight() / 20, false);

        Random random = new Random();
        int maxX = screenSizeX - bitmap.getWidth();
        x = random.nextInt(maxX);
        y = -bitmap.getHeight();

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    /*
        Updates bonus state
    */
    public void update() {
        y += speed * 7;

        collision.left = x;
        collision.top = y;
        collision.right = x + bitmap.getWidth();
        collision.bottom = y + bitmap.getHeight();
    }

    /*
        Destroys bonus when player picked up him
    */
    public void pickUp() {
        y = screenSizeY + 1;
    }

    /*
        GETTERS
    */
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BonusType getBonusType() {
        return bonusType;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getCollision() {
        return collision;
    }
}
