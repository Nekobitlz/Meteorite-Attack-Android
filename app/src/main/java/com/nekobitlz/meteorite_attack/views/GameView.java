package com.nekobitlz.meteorite_attack.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.nekobitlz.meteorite_attack.enums.EnemyType;
import com.nekobitlz.meteorite_attack.enums.GameStatus;

import com.nekobitlz.meteorite_attack.objects.*;
import com.nekobitlz.meteorite_attack.objects.enemies.Enemy;
import com.nekobitlz.meteorite_attack.objects.enemies.*;
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
    public static int ENEMY_SHIP_DESTROYED = 0;
    public static int MONEY = 0;
    public static int WEAPON_POWER;
    public static int X_SCORE = 1;

    private Thread gameThread;
    private volatile GameStatus currentStatus = GameStatus.Paused;

    private Player player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private ArrayList<Enemy> enemies;
    private AnimatedBackground background;
    private Drawer drawer;

    private int screenSizeX;
    private int screenSizeY;
    private int level;
    private int distance;
    private int fps = 0;

    private boolean isDragged = false;
    private float dragX = 0;
    private float dragY = 0;

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
        String tag = String.valueOf(spm.getPlayerImage());

        SCORE = 0;
        MONEY = 0;
        WEAPON_POWER = spm.getWeaponPower(tag);
        X_SCORE = spm.getXScore(tag);
        level = WEAPON_POWER;
        distance = 0;

        player = new Player(getContext(), screenSizeX, screenSizeY, soundPlayer);
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
        SCORE += X_SCORE;
        distance += WEAPON_POWER;

        //level up every 1000 meters
        if (distance > level * 1000) {
            level += WEAPON_POWER;
        }

        player.update();

        if (fps % 200 == 0) {
            player.fire();
        }

        enemiesUpdate(enemies);
        deleteEnemies(enemies);

        if (fps % 1000 == 0) {
            enemies.add(new Meteorite(getContext(), screenSizeX, screenSizeY, soundPlayer, level));
        }

        if (fps % 2000 == 0) {
            enemies.add(new EnemyShip(getContext(), screenSizeX, screenSizeY, soundPlayer, level));
        }

        if (distance % 750 == 0) {
            enemies.add(
                    new BorderDestroyerMeteor(getContext(), screenSizeX, screenSizeY, soundPlayer, level));
        }

        if (distance % 400 == 0) {
            enemies.add(
                    new ExploderMeteor(getContext(), screenSizeX, screenSizeY, soundPlayer, level));
        }

        //Update star background
        background.update();
        background.deleteStars();

        if (fps % 250 == 0) {
            background.createRandom();
        }
    }

    /*
        Deletes enemies from game field
    */
    private void deleteEnemies(ArrayList<Enemy> enemies) {
        boolean deleting = true;

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

    /*
        Updates enemies state
    */
    private void enemiesUpdate(ArrayList<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            enemy.update();

            //If enemy collides with spaceship -> the game ends
            //If enemy is BorderDestroyer and he went beyond the bottom line -> the game ends
            if (Rect.intersects(enemy.getCollision(), player.getCollision())
                    || (enemy.getEnemyType() == EnemyType.BorderDestroyer && enemy.getY() > screenSizeY)) {
                enemy.destroy();
                setGameOver();
            }

            //If enemy collides with laser -> gets damage
            for (Laser l : player.getLasers()) {
                if (Rect.intersects(enemy.getCollision(), l.getCollision())  && !enemy.isDestroyed()) {
                    enemy.hit();
                    l.destroy();
                }
            }
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
            spm.saveHighScore(SCORE, METEOR_DESTROYED, ENEMY_SHIP_DESTROYED);
        }
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
        Initializes and handles controls by dragging the player
    */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float evX = event.getX();
        float evY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (currentStatus == GameStatus.Playing) {
                    // If the touch is within the square
                    if (evX >= player.getX()
                            && evX <= player.getX() + player.getBitmap().getWidth()
                            && evY >= player.getY()
                            && evY <= player.getY() + player.getBitmap().getHeight()) {
                        // Activate the drag mode
                        isDragged = true;
                        dragX = evX - player.getX();
                        dragY = evY - player.getY();
                    }
                } else {
                    // When the game is over pressing the screen returns to the main menu
                    if (currentStatus == GameStatus.GameOver || currentStatus == GameStatus.NewHighScore) {
                        setMainMenuActivity();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDragged) {
                    // Defines new coordinates for drawing
                    player.setX(evX - dragX);
                    player.setY(evY - dragY);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                isDragged = false;
                break;
        }

        return true;
    }
}