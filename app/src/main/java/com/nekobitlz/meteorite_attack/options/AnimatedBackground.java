package com.nekobitlz.meteorite_attack.options;

import android.content.Context;
import com.nekobitlz.meteorite_attack.objects.Star;

import java.util.ArrayList;
import java.util.Random;

public class AnimatedBackground {
    private int screenSizeY;
    private int screenSizeX;
    private Context context;
    private ArrayList<Star> stars;

    public AnimatedBackground(Context context, int screenSizeX, int screenSizeY) {
        this.context = context;
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
    }

    public void create() {
        stars = new ArrayList<>();

        for (int i = 0; i <= 20; i++) {
            stars.add(new Star(context, screenSizeX, screenSizeY, true));
        }
    }

    public void createRandom() {
        Random random = new Random();

        for (int i = 0; i < random.nextInt(3) + 1; i++) {
            stars.add(new Star(context, screenSizeX, screenSizeY, false));
        }
    }

    public void update() {
        for (Star s : stars) {
            s.update();
        }
    }

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

    public ArrayList<Star> getStars() {
        return stars;
    }
}
