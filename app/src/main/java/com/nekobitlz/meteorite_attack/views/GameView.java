package com.nekobitlz.meteorite_attack.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.nekobitlz.meteorite_attack.enums.*;
import com.nekobitlz.meteorite_attack.objects.*;
import com.nekobitlz.meteorite_attack.options.*;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    public static int SCORE = 0;
    public static int METEOR_DESTROYED = 0;
    public static int ENEMY_DESTROYED = 0;
    public static int MONEY = 0;
    public static int WEAPON_POWER = 1;

    private Thread gameThread;
    private volatile GameStatus currentStatus = GameStatus.Paused;
    private volatile boolean newHighScore;

    private Player player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private ArrayList<Meteorite> meteors;
    private ArrayList<Enemy> enemies;
    private AnimatedBackground background;

    private int screenSizeX, screenSizeY;
    private int level;
    private int fps = 0;

    private SoundPlayer soundPlayer;
    private SharedPreferencesManager spm;

    public GameView(Context context, int screenSizeX, int screenSizeY) {
        super(context);

        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        spm = new SharedPreferencesManager(context);
        
        soundPlayer = new SoundPlayer(context);
        paint = new Paint();
        surfaceHolder = getHolder();
        
        reset();
    }

    private void reset() {
        SCORE = 0;
        MONEY = 0;
        level = 1;

        player = new Player(getContext(), screenSizeX, screenSizeY, soundPlayer);
        meteors = new ArrayList<>();
        enemies = new ArrayList<>();
        background = new AnimatedBackground(getContext(), screenSizeX, screenSizeY);

        //create star background
        background.create();

        currentStatus = GameStatus.Playing;
        newHighScore = false;
    }

    @Override
    public void run() {
        while (currentStatus == GameStatus.Playing) {
            update();
            draw();
            control();
        }
    }

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

    private void setGameOver() {
        currentStatus = GameStatus.GameOver;
        MONEY += SCORE / 100;
        spm.saveMoney(MONEY);

        //Saves new high score
        if (SCORE > spm.getHighScore()) {
            newHighScore = true;
            spm.saveHighScore(SCORE, METEOR_DESTROYED, ENEMY_DESTROYED);
        }
    }

    public void setDirection(Direction currentDirection, float speed) {
        player.setCurrentDirection(currentDirection, speed);
    }

    //Playing field update frequency
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

    public void pause() {
        currentStatus = GameStatus.Paused;

        try {
            gameThread.join();
            soundPlayer.pause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        currentStatus = GameStatus.Playing;
        soundPlayer.resume();

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            //Draw player
            canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), paint);

            for (Star s: background.getStars()) {
                canvas.drawBitmap(s.getBitmap(), s.getX(), s.getY(), paint);
            }

            for (Laser l: player.getLasers()) {
                canvas.drawBitmap(l.getBitmap(), l.getX(), l.getY(), paint);
            }

            for (Meteorite m: meteors) {
                canvas.drawBitmap(m.getBitmap(), m.getX(), m.getY(), paint);
                drawHealth(m, m.getX(), m.getY(), paint);
            }

            for (Enemy e: enemies) {
                canvas.drawBitmap(e.getBitmap(), e.getX(), e.getY(), paint);
                drawHealth(e, e.getX(), e.getY(), paint);
            }

            drawScore();

            if (currentStatus == GameStatus.GameOver) {
                drawGameOver();
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawHealth(Meteorite meteor, int x, int y, Paint paint) {
        Bitmap meteorBitmap = meteor.getBitmap();

        setHealthPaintSettings();

        canvas.drawText("" + meteor.getHealth(), x + meteorBitmap.getWidth() / 2 - 10,
                y + meteorBitmap.getHeight() / 2, paint);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawHealth(Enemy enemy, int x, int y, Paint paint) {
        Bitmap enemyBitmap = enemy.getBitmap();

        setHealthPaintSettings();

        canvas.drawText("" + enemy.getHealth(), x + enemyBitmap.getWidth() / 2 - 10,
                y + enemyBitmap.getHeight() / 2, paint);
    }

    private void setHealthPaintSettings() {
        paint.setTextSize(40);
        paint.setColor(Color.WHITE);
        paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        paint.setShadowLayer(0.1f, -2, 2, Color.BLACK);
    }

    private void drawScore() {
        Paint score = new Paint();
        score.setTextSize(30);
        score.setColor(Color.WHITE);

        canvas.drawText("Score : " + SCORE, 100, 50, score);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawGameOver() {
        Paint gameOver = new Paint();
        gameOver.setTextSize(100);
        gameOver.setTextAlign(Paint.Align.CENTER);
        gameOver.setColor(Color.WHITE);

        canvas.drawText("GAME OVER", screenSizeX / 2, screenSizeY / 2, gameOver);

        Paint money = new Paint();
        money.setTextSize(50);
        money.setTextAlign(Paint.Align.CENTER);
        money.setColor(Color.WHITE);

        canvas.drawText("Money : " + MONEY, screenSizeX / 2, (screenSizeY / 2) + 60, money);

        //Draw high score
        Paint highScore = new Paint();
        highScore.setTextSize(50);
        highScore.setTextAlign(Paint.Align.CENTER);
        highScore.setColor(Color.WHITE);

        //Draw new high score and stat
        if (newHighScore) {
            canvas.drawText("New High Score : " + spm.getHighScore(),
                    screenSizeX / 2, (screenSizeY / 2) + 120, highScore);

            Paint enemyDestroyed = new Paint();
            enemyDestroyed.setTextSize(50);
            enemyDestroyed.setTextAlign(Paint.Align.CENTER);
            enemyDestroyed.setColor(Color.WHITE);

            canvas.drawText(
                    "Enemy Destroyed : " + spm.getEnemyDestroyed(),
                    screenSizeX / 2, (screenSizeY / 2) + 180, enemyDestroyed);

            Paint meteorDestroyed = new Paint();
            meteorDestroyed.setTextSize(50);
            meteorDestroyed.setTextAlign(Paint.Align.CENTER);
            meteorDestroyed.setColor(Color.WHITE);

            canvas.drawText("Meteor Destroyed : " + spm.getMeteorDestroyed(),
                    screenSizeX / 2, (screenSizeY / 2) + 240, meteorDestroyed);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //When game is over - touch the screen tosses into main menu
            case MotionEvent.ACTION_DOWN:
                if (currentStatus == GameStatus.GameOver) {
                    ((Activity) getContext()).finish();
                    getContext().startActivity(new Intent(getContext(), MainMenuActivity.class));
                }
                break;
        }

        return super.onTouchEvent(event);
    }
}
