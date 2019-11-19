package com.nekobitlz.meteorite_attack.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;

/*
    Bullet that the player shoots
*/
public class Laser {

    private Bitmap bitmap;
    private SharedPreferencesManager spm;
    private Rect collision;

    private int x;
    private int y;

    /*
        Laser initialization
    */
    public Laser(Context context, int playerX, int playerY, Bitmap playerBitmap) {
        spm = new SharedPreferencesManager(context);

        //Set laser image
        bitmap = BitmapFactory.decodeResource(context.getResources(), spm.getLaserImage());
        bitmap = Bitmap.createScaledBitmap(
                bitmap, bitmap.getWidth() * 3 / 5, bitmap.getHeight() * 3 / 5, false);

        x = playerX + playerBitmap.getWidth() / 2 - bitmap.getWidth() / 2;
        y = playerY - bitmap.getHeight() - 10;

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    /*
        Updates laser state
    */
    public void update() {
        y -= bitmap.getHeight() - 10;

        collision.left = x;
        collision.top = y;
        collision.right = x + bitmap.getWidth();
        collision.bottom = y + bitmap.getHeight();
    }

    /*
        Destroys laser
    */
    public void destroy() {
        y = -bitmap.getHeight();
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

    public Rect getCollision() {
        return collision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
