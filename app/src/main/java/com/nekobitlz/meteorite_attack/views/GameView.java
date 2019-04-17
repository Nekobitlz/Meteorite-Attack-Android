package com.nekobitlz.meteorite_attack.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.nekobitlz.meteorite_attack.enums.GameStatus;
import com.nekobitlz.meteorite_attack.enums.Objects;

import com.nekobitlz.meteorite_attack.objects.*;
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

    private ArrayList<Meteorite> meteors;
    private ArrayList<BorderDestroyerMeteor> borderDestroyerMeteors;
    private ArrayList<EnemyShip> enemyShips;
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
        meteors = new ArrayList<>();
        borderDestroyerMeteors = new ArrayList<>();
        enemyShips = new ArrayList<>();
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
        drawer.setEnemyShips(enemyShips);
        drawer.setMeteors(meteors);
        drawer.setPlayer(player);
        drawer.setBorderDestroyers(borderDestroyerMeteors);
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

        objectsUpdate(Objects.Meteorite);
        deleteObjects(Objects.Meteorite);

        if (fps % 1000 == 0) {
            meteors.add(new Meteorite(getContext(), screenSizeX, screenSizeY, soundPlayer, level));
        }

        objectsUpdate(Objects.EnemyShip);
        deleteObjects(Objects.EnemyShip);

        if (fps % 2000 == 0) {
            enemyShips.add(new EnemyShip(getContext(), screenSizeX, screenSizeY, soundPlayer, level));
        }

        objectsUpdate(Objects.BorderDestroyer);
        deleteObjects(Objects.BorderDestroyer);

        if (distance % 2000 == 0) {
            borderDestroyerMeteors.add(
                    new BorderDestroyerMeteor(getContext(), screenSizeX, screenSizeY, soundPlayer, level));
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
            case EnemyShip: {
                while (deleting) {
                    if (enemyShips.size() != 0) {
                        if (enemyShips.get(0).getY() > screenSizeY) {
                            enemyShips.remove(0);
                        }
                    }

                    if (enemyShips.size() == 0 || enemyShips.get(0).getY() <= screenSizeY) {
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

            case BorderDestroyer: {
                while (deleting) {
                    if (borderDestroyerMeteors.size() != 0) {
                        if (borderDestroyerMeteors.get(0).getY() > screenSizeY) {
                            borderDestroyerMeteors.remove(0);
                        }
                    }

                    if (borderDestroyerMeteors.size() == 0 || borderDestroyerMeteors.get(0).getY() <= screenSizeY) {
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

                    //If Meteorite collides with spaceship -> the game ends
                    if (Rect.intersects(m.getCollision(), player.getCollision())) {
                        m.destroy();
                        setGameOver();
                    }

                    //If Meteorite collides with laser -> gets damage
                    for (Laser l : player.getLasers()) {
                        if (Rect.intersects(m.getCollision(), l.getCollision())) {
                            m.hit();
                            l.destroy();
                        }
                    }
                }
            }
            break;

            case EnemyShip: {
                for (EnemyShip e : enemyShips) {
                    e.update();

                    //If EnemyShip collides with spaceship -> the game ends
                    if (Rect.intersects(e.getCollision(), player.getCollision())) {
                        e.destroy();
                        setGameOver();
                    }

                    //If EnemyShip collides with laser -> gets damage
                    for (Laser l : player.getLasers()) {
                        if (Rect.intersects(e.getCollision(), l.getCollision())) {
                            e.hit();
                            l.destroy();
                        }
                    }
                }
            }
            break;

            case BorderDestroyer: {
                for (BorderDestroyerMeteor bdm : borderDestroyerMeteors) {
                    bdm.update();

                    //If "Border destroyer" collides with spaceship or he flew off the screen -> the game ends
                    if (Rect.intersects(bdm.getCollision(), player.getCollision()) || bdm.getY() > screenSizeY) {
                        bdm.destroy();
                        setGameOver();
                    }

                    //If "Border destroyer" collides with laser -> gets damage
                    for (Laser l : player.getLasers()) {
                        if (Rect.intersects(bdm.getCollision(), l.getCollision())) {
                            bdm.hit();
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