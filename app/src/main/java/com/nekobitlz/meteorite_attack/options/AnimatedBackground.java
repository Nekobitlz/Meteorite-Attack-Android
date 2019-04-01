package com.nekobitlz.meteorite_attack.options;

import android.content.Context;
import com.nekobitlz.meteorite_attack.objects.Star;

import java.util.ArrayList;
import java.util.Random;

/*
    Animated background with stars while playing
*/
public class AnimatedBackground {
    private int screenSizeY;
    private int screenSizeX;
    private Context context;
    private ArrayList<Star> stars;

    /*
        Background initialization
    */
    public AnimatedBackground(Context context, int screenSizeX, int screenSizeY) {
        this.context = context;
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
    }

    /*
        Creates background
    */
    public void create() {
        stars = new ArrayList<>();

        for (int i = 0; i <= 20; i++) {
            stars.add(new Star(context, screenSizeX, screenSizeY, true));
        }
    }

    /*
       Creates random star position on background
    */
    public void createRandom() {
        Random random = new Random();

        for (int i = 0; i < random.nextInt(3) + 1; i++) {
            stars.add(new Star(context, screenSizeX, screenSizeY, false));
        }
    }

    /*
        Updates stars on background stay
    */
    public void update() {
        for (Star s : stars) {
            s.update();
        }
    }

    /*
        Deletes stars from background
    */
    public void deleteStars() {
        boolean deleting = true;

        while (deleting) {
            if (stars.size() != 0) {
                if (stars.get(0).getY() > screenSizeY) {
                    stars.remove(0);
                }
            }

            if (stars.size() == 0 || stars.get(0).getY() <= screenSizeY)
                deleting = false;
        }
    }

    /*
        GETTERS
    */
    public ArrayList<Star> getStars() {
        return stars;
    }
}
