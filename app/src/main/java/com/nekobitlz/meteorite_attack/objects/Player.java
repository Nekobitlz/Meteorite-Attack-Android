package com.nekobitlz.meteorite_attack.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.options.SoundPlayer;

import java.util.ArrayList;

/*
  The main player to be managed by the user
*/
public class Player {

    private Bitmap bitmap;

    private float x;
    private float y;
    private int screenSizeX;
    private int screenSizeY;

    private int margin = 16; //indent from edge
    private Rect collision;
    private SharedPreferencesManager spm;
    private int health;

    private ArrayList<Laser> lasers;
    private Context context;
    private SoundPlayer soundPlayer;

    /*
        Player initialization
    */
    public Player(Context context, int screenSizeX, int screenSizeY, SoundPlayer soundPlayer) {
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        this.context = context;
        this.soundPlayer = soundPlayer;
        spm = new SharedPreferencesManager(context);

        bitmap = BitmapFactory.decodeResource(context.getResources(), spm.getPlayerImage());
        bitmap = Bitmap.createScaledBitmap(
                bitmap, bitmap.getWidth() * 3 / 5, bitmap.getHeight() * 3 / 5, false);

        x = screenSizeX / 2 - bitmap.getWidth() / 2;
        y = screenSizeY - bitmap.getHeight() - margin;
        health = spm.getHealth(String.valueOf(spm.getPlayerImage()));

        lasers = new ArrayList<>();

        collision = new Rect((int) x, (int) y, (int) x + bitmap.getWidth(), (int) y + bitmap.getHeight());
    }

    /*
        Updates player state
    */
    public void update() {
        collision.left = (int) x;
        collision.top = (int) y;
        collision.right = (int) (x + bitmap.getWidth());
        collision.bottom = (int) (y + bitmap.getHeight());

        for (Laser l : lasers) {
            l.update();
        }

        deleteLasers();
    }

    /*
        Remove lasers off the screen
    */
    private void deleteLasers() {
        boolean deleting = true;

        while (deleting) {
            if (lasers.size() != 0) {
                if (lasers.get(0).getY() < 0) {
                    lasers.remove(0);
                }
            }

            if (lasers.size() == 0 || lasers.get(0).getY() >= 0) {
                deleting = false;
            }
        }
    }

    /*
        Add lasers on the playing field
    */
    public void fire() {
        lasers.add(new Laser(context, screenSizeX, screenSizeY, (int) x, (int) y, bitmap));
        soundPlayer.playLaser();
    }

    /*
        GETTERS & SETTERS
    */
    public Rect getCollision() {
        return collision;
    }

    public ArrayList<Laser> getLasers() {
        return lasers;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
