package com.nekobitlz.meteorite_attack.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import com.nekobitlz.meteorite_attack.R;

public class Laser {

    private Bitmap bitmap;

    private int x;
    private int y;
    private int screenSizeX;
    private int screenSizeY;

    private Rect collision;
    private boolean isEnemy;

    public Laser(
            Context context,
            int screenSizeX,
            int screenSizeY,
            int playerX,
            int playerY,
            Bitmap playerBitmap,
            boolean isEnemy) {
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        this.isEnemy = isEnemy;

        //Set laser image
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.laser_2);
        bitmap = Bitmap.createScaledBitmap(
                bitmap, bitmap.getWidth() * 3 / 5, bitmap.getHeight() * 3 / 5, false);

        x = playerX + playerBitmap.getWidth() / 2 - bitmap.getWidth() / 2;

        if (isEnemy)
            y = playerY + bitmap.getHeight() + 10;
        else
            y = playerY - bitmap.getHeight() - 10;

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    public void update() {
        if (isEnemy)
            y += bitmap.getHeight() + 10;
        else
            y -= bitmap.getHeight() - 10;

        collision.left = x;
        collision.top = y;
        collision.right = x + bitmap.getWidth();
        collision.bottom = y + bitmap.getHeight();
    }

    public void destroy() {
        if (isEnemy)
            y = screenSizeY;
        else
            y = -bitmap.getHeight();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rect getCollision() {
        return collision;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
