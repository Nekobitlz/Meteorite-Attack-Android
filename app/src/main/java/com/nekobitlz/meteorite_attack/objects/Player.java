package com.nekobitlz.meteorite_attack.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.enums.Direction;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.options.SoundPlayer;

import java.util.ArrayList;

import static java.lang.Math.abs;

/*
  The main player to be managed by the user
*/
public class Player {

    private Bitmap bitmap;

    private int x;
    private int y;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private int screenSizeX, screenSizeY;

    private int margin = 16; //indent from edge
    private int speed;

    private Direction currentDirection = Direction.Stopped;
    private float headingSpeed;
    private Rect collision;
    private SharedPreferencesManager spm;

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

        maxX = screenSizeX - bitmap.getWidth();
        maxY = screenSizeY - bitmap.getHeight();
        minX = 0;
        minY = 0;
        speed = 1;

        x = screenSizeX / 2 - bitmap.getWidth() / 2;
        y = screenSizeY - bitmap.getHeight() - margin;

        lasers = new ArrayList<>();

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    /*
        Updates player state
    */
    public void update() {
        if (currentDirection == Direction.Left) {
            x -= headingSpeed * 10;

            if (x < minX)
                x = minX;
        } else if (currentDirection == Direction.Right) {
            x += headingSpeed * 10;

            if (x > maxX)
                x = maxX;
        }

        collision.left = x;
        collision.top = y;
        collision.right = x + bitmap.getWidth();
        collision.bottom = y + bitmap.getHeight();

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
        lasers.add(new Laser(context, screenSizeX, screenSizeY, x, y, bitmap));
        soundPlayer.playLaser();
    }

    /*
        Sets direction for player
    */
    public void setCurrentDirection(Direction direction, float speed) {
        currentDirection = direction;
        headingSpeed = abs(speed);
    }

    /*
        GETTERS
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
