package com.nekobitlz.meteorite_attack.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.support.v4.app.FragmentManager;
import android.view.*;

import com.nekobitlz.meteorite_attack.enums.BonusType;
import com.nekobitlz.meteorite_attack.enums.EnemyType;
import com.nekobitlz.meteorite_attack.enums.GameStatus;

import com.nekobitlz.meteorite_attack.objects.*;
import com.nekobitlz.meteorite_attack.objects.Bonus;
import com.nekobitlz.meteorite_attack.objects.enemies.Enemy;
import com.nekobitlz.meteorite_attack.objects.enemies.*;
import com.nekobitlz.meteorite_attack.options.AnimatedBackground;
import com.nekobitlz.meteorite_attack.options.Drawer;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.options.SoundPlayer;

import java.util.ArrayList;
import java.util.Random;

/*
    MAIN GAME ENGINE
*/
public class GameView extends SurfaceView implements Runnable {

    public static int SCORE = 0;
    public static int METEOR_DESTROYED = 0;
    public static int ENEMY_SHIP_DESTROYED = 0;
    public static int EXPLODER_DESTROYED = 0;
    public static int BORDER_DESTROYER_DESTROYED = 0;
    public static int MONEY = 0;
    public static int WEAPON_POWER = 1;
    public static int X_SCORE = 1;
    public static int BONUS_DURATION = 0;

    private Thread gameThread;
    private volatile GameStatus currentStatus = GameStatus.Paused;

    private Context context;
    private Player player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private ArrayList<Enemy> enemies;
    private ArrayList<Bonus> bonuses;
    private AnimatedBackground background;
    private Drawer drawer;

    private String tag;
    private int screenSizeX;
    private int screenSizeY;
    private int level;
    private int distance;
    private int fps = 0;
    private int meteorFreq;

    // Upgrade settings
    private int shotSpeed;
    private boolean tripleShotMode;
    private boolean isBonusEnabled;

    // Drag & drop
    private boolean isDragged = false;
    private float dragX = 0;
    private float dragY = 0;

    private SoundPlayer soundPlayer;
    private SharedPreferencesManager spm;

    private long beforeTime;
    private long sleepTime;
    private long delay = 30;
    private GameOverFragment gameOverFragment;

    /*
        Game engine initialization
    */
    public GameView(Context context, int screenSizeX, int screenSizeY) {
        super(context);

        this.context = context;
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        spm = new SharedPreferencesManager(context);

        soundPlayer = new SoundPlayer(context);
        paint = new Paint();
        surfaceHolder = getHolder();

        drawer = new Drawer(context, screenSizeX, screenSizeY, canvas, paint, surfaceHolder, spm);
        initDrawer();

        reset();
    }

    /*
        Resets all settings for new game
    */
    private void reset() {
        tag = String.valueOf(spm.getPlayerImage());

        SCORE = 0;
        MONEY = 0;
        WEAPON_POWER = spm.getWeaponPower(tag);
        X_SCORE = spm.getXScore(tag);
        BONUS_DURATION = 0;
        METEOR_DESTROYED = 0;
        ENEMY_SHIP_DESTROYED = 0;
        BORDER_DESTROYER_DESTROYED = 0;
        EXPLODER_DESTROYED = 0;

        level = spm.getLevel();
        distance = 0;
        meteorFreq = 500;

        shotSpeed = 200;
        tripleShotMode = false;
        isBonusEnabled = false;

        player = new Player(getContext(), screenSizeX, screenSizeY, soundPlayer);
        enemies = new ArrayList<>();
        bonuses = new ArrayList<>();
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
        drawer.setBonuses(bonuses);
    }

    /*
        Runs game
    */
    @Override
    public void run() {
        while (currentStatus == GameStatus.Playing) {
            beforeTime = System.nanoTime();
            update();
            drawer.draw();
            control();
        }
    }

    /*
        Updates game state
    */
    public void update() {
        SCORE += X_SCORE;
        distance += level;

        //level up every 1000 meters
        if (distance > level * 1000) {
            level++;
        }

        player.update();

        if (fps % shotSpeed == 0) {
            player.fire(tripleShotMode);
        }

        enemiesUpdate(enemies);
        deleteEnemies(enemies);

        if (fps % meteorFreq == 0) {
            enemies.add(new Meteorite(getContext(), screenSizeX, screenSizeY, soundPlayer, level));
        }

        if (fps % 2000 == 0) {
            enemies.add(new EnemyShip(getContext(), screenSizeX, screenSizeY, soundPlayer, level));
        }

        if (distance % (level * 700) == 0) {
            enemies.add(
                    new BorderDestroyerMeteor(getContext(), screenSizeX, screenSizeY, soundPlayer, level)
            );
        }

        if (distance % (level * 300) == 0) {
            enemies.add(
                    new ExploderMeteor(getContext(), screenSizeX, screenSizeY, soundPlayer, level)
            );
        }

        //Bonuses update
        bonusUpdate(bonuses);

        if (distance % (level * 350) == 0) {
            //Random bonus pick
            int pick = new Random().nextInt(BonusType.values().length);
            BonusType bonusType = BonusType.values()[pick];

            bonuses.add(new Bonus(getContext(), screenSizeX, screenSizeY, soundPlayer, bonusType));
        }

        //Limited bonus time
        if (isBonusEnabled) {
            if (BONUS_DURATION < 500) {
                BONUS_DURATION++;
            } else {
                isBonusEnabled = false;
                resetBonuses();
            }
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

            /*
                If player collides with enemy and health of enemy is greater than health of player
                                              or enemy is BorderDestroyer -> game ends,
                otherwise player receives damage equal to enemy health
            */
            if (Rect.intersects(enemy.getCollision(), player.getCollision())
                    || (enemy.getEnemyType() == EnemyType.BorderDestroyer
                    && enemy.getY() > screenSizeY && !enemy.isDestroyed())) {
                int playerHealth = player.getHealth() - enemy.getHealth();

                if (enemy.getHealth() >= player.getHealth()
                        || (enemy.getEnemyType() == EnemyType.BorderDestroyer && enemy.getY() > screenSizeY)) {
                    player.setHealth(playerHealth);
                    enemy.destroy();
                    setGameOver();
                } else {
                    //If a player enters the crater, he takes damage in 1 health unit
                    if (enemy.isDestroyed() && enemy.getEnemyType() == EnemyType.Exploder) {
                        player.setHealth(player.getHealth() - 1);

                        if (player.getHealth() == 0) {
                            setGameOver();
                        }
                    } else {
                        player.setHealth(playerHealth);
                        enemy.destroy();
                    }
                }
            }

            //If enemy collides with laser -> enemy gets damage
            for (Laser l : player.getLasers()) {
                if (Rect.intersects(enemy.getCollision(), l.getCollision())  && !enemy.isDestroyed()) {
                    enemy.hit();
                    l.destroy();
                }
            }
        }
    }

    /*
        Updates bonuses state
    */
    private void bonusUpdate(ArrayList<Bonus> bonuses) {
        for (Bonus bonus: bonuses) {
            bonus.update();

            if (Rect.intersects(bonus.getCollision(), player.getCollision())) {
                bonus.pickUp();
                resetBonuses();

                switch (bonus.getBonusType()) {
                    //Increases health 2 times
                    case Health: {
                        player.setHealth(player.getHealth() * 2);
                    }
                    break;

                    //Increases shooting speed 2 times
                    case Speed_up: {
                        shotSpeed /= 2;
                        isBonusEnabled = true;
                    }
                    break;

                    //Increases the number of lasers 3 times
                    case Triple_shot: {
                        tripleShotMode = true;
                        isBonusEnabled = true;
                    }
                    break;

                    //Decreases shooting speed 2 times and
                    case Slow_up: {
                        shotSpeed *= 2;
                        WEAPON_POWER += WEAPON_POWER / 10 + 5;
                        isBonusEnabled = true;
                    }
                    break;

                    /*If a player’s health is greater than 1 -> his health decreases to 1
                                                             and weapon power multiplied by 2 times
                      If less -> he kills him.
                    */
                    case Destroyer: {
                        if (player.getHealth() > 1) {
                            player.setHealth(1);
                            WEAPON_POWER *= 2;
                        } else {
                            player.setHealth(0);
                            setGameOver();
                        }
                    }
                    break;
                }
            }
        }
    }

    /*
        Resets status of all bonuses
    */
    private void resetBonuses() {
        BONUS_DURATION = 0;
        shotSpeed = 200;
        tripleShotMode = false;
        isBonusEnabled = false;
        WEAPON_POWER = spm.getWeaponPower(tag);
    }

    private void setGameOver() {
        currentStatus = GameStatus.GameOver;
        MONEY += SCORE / 100;
        spm.saveMoney(MONEY);

        //Saves new high score
        if (SCORE > spm.getHighScore()) {
            currentStatus = GameStatus.NewHighScore;
            spm.saveHighScore(
                    SCORE, METEOR_DESTROYED, ENEMY_SHIP_DESTROYED,
                    BORDER_DESTROYER_DESTROYED, EXPLODER_DESTROYED
            );
        }

        //Show game over fragment
        gameOverFragment = GameOverFragment.newInstance();
        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();

        gameOverFragment.setCancelable(false);
        gameOverFragment.show(fragmentManager, "gameOverFragment");
    }

    /*
        Playing field update frequency
    */
    public void control() {
        /*
          Time required to sleep to keep game consistent
          This allows game to render smoothly
        */
        sleepTime = delay - ((System.nanoTime() - beforeTime) / 1000000L);

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

    /*
        Pauses the game
    */
    public void pause() {
        if (currentStatus != GameStatus.GameOver && currentStatus != GameStatus.NewHighScore) {
            currentStatus = GameStatus.Paused;
        }

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
        if (currentStatus != GameStatus.GameOver && currentStatus != GameStatus.NewHighScore) {
            currentStatus = GameStatus.Playing;
        }

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

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    /*
         !package-private!
        Sets main menu activity and clears sounds pool
    */
    void setMainMenuActivity() {
        gameOverFragment.dismiss();
        ((Activity) context).finish();
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
                }
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