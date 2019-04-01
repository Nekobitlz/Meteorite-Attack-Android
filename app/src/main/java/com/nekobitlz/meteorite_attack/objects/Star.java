package com.nekobitlz.meteorite_attack.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.nekobitlz.meteorite_attack.R;

import java.util.Random;

/*
    Star is just for ANIMATED BACKGROUND
*/
public class Star {

    private Bitmap bitmap;

    private int x;
    private int y;
    private int maxX;
    private int screenSizeX;
    private int screenSizeY;

    private int speed;
    private int[] stars = new int[] { R.drawable.star_1, R.drawable.star_2, R.drawable.star_3 }; //TODO(): More stars

    /*
        Star initialization
    */
    public Star(Context context, int screenSizeX, int screenSizeY, boolean randomY){
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;

        Random random = new Random();
        float scale = (float)(random.nextInt(3) + 1) / 5;

        //Set random image of star
        bitmap = BitmapFactory.decodeResource(context.getResources(), stars[random.nextInt(3)]);
        bitmap = Bitmap.createScaledBitmap(
                bitmap, (int)(bitmap.getWidth() * scale), (int)(bitmap.getHeight() * scale), false);

        maxX = screenSizeX - bitmap.getWidth();
        x = random.nextInt(maxX);

        speed = random.nextInt(1) + 1;

        if (randomY)
            y = random.nextInt(screenSizeY);
        else
            y = -bitmap.getHeight();
    }

    /*
        Updates star state
    */
    public void update(){
        y += 7 * speed;
    }

    /*
        GETTERS
    */
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
