package com.nekobitlz.meteorite_attack.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.nekobitlz.meteorite_attack.options.AnimatedBackground;
import com.nekobitlz.meteorite_attack.options.Drawer;

public class StarsView extends SurfaceView implements Runnable {

    private AnimatedBackground background;
    private Thread gameThread;

    private Context context;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private int screenSizeX;
    private int screenSizeY;
    private Drawer drawer;
    private long beforeTime;
    private int fps = 0;
    private boolean isRunning;


    public StarsView(Context context, int screenSizeX, int screenSizeY) {
        super(context);

        this.context = context;
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;

        paint = new Paint();
        surfaceHolder = getHolder();
        background = new AnimatedBackground(context, screenSizeX, screenSizeY);
        background.create();

        drawer = new Drawer(context, screenSizeX, screenSizeY, canvas, paint, surfaceHolder, background);
    }

    @Override
    public void run() {
        while (isRunning) {
            beforeTime = System.nanoTime();

            update();
            drawer.drawBackground();
            control();
        }
    }

    private void update() {
        background.update();
        background.deleteStars();

        if (fps % 250 == 0) {
            background.createRandom();
        }
    }

    public void control() {
        /*
          Time required to sleep to keep game consistent
          This allows game to render smoothly
        */
        long sleepTime = 30 - ((System.nanoTime() - beforeTime) / 1000000L);

        try {
            if (fps == 10000) {
                fps = 0;
            }

            if (sleepTime > 0) {
                Thread.sleep(sleepTime);
            }

            fps += 20;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        isRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
