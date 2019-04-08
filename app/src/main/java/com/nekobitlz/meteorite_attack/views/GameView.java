package com.nekobitlz.meteorite_attack.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.nekobitlz.meteorite_attack.enums.Direction;
import com.nekobitlz.meteorite_attack.enums.GameStatus;
import com.nekobitlz.meteorite_attack.enums.Objects;

import com.nekobitlz.meteorite_attack.objects.Enemy;
import com.nekobitlz.meteorite_attack.objects.Laser;
import com.nekobitlz.meteorite_attack.objects.Meteorite;
import com.nekobitlz.meteorite_attack.objects.Player;
import com.nekobitlz.meteorite_attack.options.AnimatedBackground;
import com.nekobitlz.meteorite_attack.options.Drawer;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.options.SoundPlayer;

import java.util.ArrayList;

/*
    MAIN GAME ENGINE
*/
public class GameView extends SurfaceView implements Runnable {

    public static int SCORE = 0;
    public static int METEOR_DESTROYED = 0;
    public static int ENEMY_DESTROYED = 0;
    public static int MONEY = 0;
    public static int WEAPON_POWER;

    private Thread gameThread;
    private volatile GameStatus currentStatus = GameStatus.Paused;

    private Player player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private ArrayList<Meteorite> meteors;
    private ArrayList<Enemy> enemies;
    private AnimatedBackground background;
    private Drawer drawer;

    private int screenSizeX;
    private int screenSizeY;
    private int level;
    private int fps = 0;

    private SoundPlayer soundPlayer;
    private SharedPreferencesManager spm;

    /*
        Game engine initialization
    */
    public GameView(Context context, int screenSizeX, int screenSizeY) {
        super(context);

        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        spm = new SharedPreferencesManager(context);

        soundPlayer = new SoundPlayer(context);
        paint = new Paint();
        surfaceHolder = getHolder();

        drawer = new Drawer(screenSizeX, screenSizeY, canvas, paint, surfaceHolder, spm);
        initDrawer();

        reset();
    }

    /*
        Resets all settings for new game
    */
    private void reset() {
        SCORE = 0;
        MONEY = 0;
        WEAPON_POWER = spm.getWeaponPower();
        level = 1;

        player = new Player(getContext(), screenSizeX, screenSizeY, soundPlayer);
        meteors = new ArrayList<>();
        enemies = new ArrayList<>();
        background = new AnimatedBackground(getContext(), screenSizeX, screenSizeY);

        //create star background
        background.create();
        initDrawer();

        currentStatus = GameStatus.Playing;
    }

    /*
        Drawer initialization
    */
    private void initDrawer() {
        drawer.setBackground(background);
        drawer.setEnemies(enemies);
        drawer.setMeteors(meteors);
        drawer.setPlayer(player);
    }

    /*
        Runs game
    */
    @Override
    public void run() {
        while (currentStatus == GameStatus.Playing) {
            update();
            drawer.draw(currentStatus);
            control();
        }
    }

    /*
        Updates game state
    */
    public void update() {
        SCORE++;

        //level up every 1000 points
        if (SCORE > level * 1000) {
            level++;
        }

        player.update();

        if (fps % 200 == 0) {
            player.fire();
        }

        objectsUpdate(Objects.Meteorite);
        deleteObjects(Objects.Meteorite);

        if (fps % 1000 == 0) {
            meteors.add(new Meteorite(getContext(), screenSizeX, screenSizeY, soundPlayer, level));
        }

        objectsUpdate(Objects.Enemy);
        deleteObjects(Objects.Enemy);

        if (fps % 2000 == 0) {
            enemies.add(new Enemy(getContext(), screenSizeX, screenSizeY, soundPlayer, level));
        }

        //Update star background
        background.update();
        background.deleteStars();

        if (fps % 250 == 0) {
            background.createRandom();
        }
    }

    /*
        Deletes objects from game field
    */
    private void deleteObjects(Objects objectName) {
        boolean deleting = true;

        switch (objectName) {
            case Enemy: {
                while (deleting) {
                    if (enemies.size() != 0) {
                        if (enemies.get(0).getY() > screenSizeY) {
                            enemies.remove(0);
                        }
                    }

                    if (enemies.size() == 0 || enemies.get(0).getY() <= screenSizeY) {
                        deleting = false;
                    }
                }
            }
            break;

            case Meteorite: {
                while (deleting) {
                    if (meteors.size() != 0) {
                        if (meteors.get(0).getY() > screenSizeY) {
                            meteors.remove(0);
                        }
                    }

                    if (meteors.size() == 0 || meteors.get(0).getY() <= screenSizeY) {
                        deleting = false;
                    }
                }
            }
            break;
        }
    }

    /*
        Updates objects state
    */
    private void objectsUpdate(Objects objectName) { //TODO(): Optimize method
        switch (objectName) {
            case Meteorite: {
                for (Meteorite m : meteors) {
                    m.update();

                    //Meteorite collides with spaceship and the game ends
                    if (Rect.intersects(m.getCollision(), player.getCollision())) {
                        m.destroy();
                        setGameOver();
                    }

                    //Meteorite collides with laser and gets damage
                    for (Laser l : player.getLasers()) {
                        if (Rect.intersects(m.getCollision(), l.getCollision())) {
                            m.hit();
                            l.destroy();
                        }
                    }
                }
            }
            break;

            case Enemy: {
                for (Enemy e : enemies) {
                    e.update();

                    //Enemy collides with spaceship and the game ends
                    if (Rect.intersects(e.getCollision(), player.getCollision())) {
                        e.destroy();
                        setGameOver();
                    }

                    //Enemy collides with laser and gets damage
                    for (Laser l : player.getLasers()) {
                        if (Rect.intersects(e.getCollision(), l.getCollision())) {
                            e.hit();
                            l.destroy();
                        }
                    }
                }
            }
            break;
        }
    }

    /*
        Sets game over state
    */
    private void setGameOver() {
        currentStatus = GameStatus.GameOver;
        MONEY += SCORE / 100;
        spm.saveMoney(MONEY);

        //Saves new high score
        if (SCORE > spm.getHighScore()) {
            currentStatus = GameStatus.NewHighScore;
            spm.saveHighScore(SCORE, METEOR_DESTROYED, ENEMY_DESTROYED);
        }
    }

    /*
        Sets player direction
    */
    public void setDirection(Direction currentDirection, float speed) {
        player.setCurrentDirection(currentDirection, speed);
    }

    /*
        Playing field update frequency
    */
    public void control() {
        try {
            if (fps == 10000) {
                fps = 0;
            }

            Thread.sleep(20);
            fps += 20;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
        Pauses the game
    */
    public void pause() {
        currentStatus = GameStatus.Paused;

        try {
            gameThread.join();
            soundPlayer.pause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
        Resumes the game
    */
    public void resume() {
        currentStatus = GameStatus.Playing;
        soundPlayer.resume();

        gameThread = new Thread(this);
        gameThread.start();
    }

    /*
        GETTERS
    */
    public GameStatus getCurrentStatus() {
        return currentStatus;
    }

    /*
         !package-private!
        Sets main menu activity and clears sounds pool
    */
    void setMainMenuActivity() {
        ((Activity) getContext()).finish();
        soundPlayer.release();
    }

    /*
        Reads buttons presses
    */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //When game is over - touch the screen tosses into main menu
            case MotionEvent.ACTION_DOWN: {
                if (currentStatus == GameStatus.GameOver || currentStatus == GameStatus.NewHighScore) {
                    setMainMenuActivity();
                }
            }
            break;
        }

        return super.onTouchEvent(event);
    }
}