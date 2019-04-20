package com.nekobitlz.meteorite_attack.options;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import com.nekobitlz.meteorite_attack.enums.GameStatus;
import com.nekobitlz.meteorite_attack.objects.*;
import com.nekobitlz.meteorite_attack.objects.enemies.Enemy;

import java.util.ArrayList;

import static com.nekobitlz.meteorite_attack.views.GameView.MONEY;
import static com.nekobitlz.meteorite_attack.views.GameView.SCORE;

/*
     Draws all the drawings
*/
public class Drawer {
    private int screenSizeX;
    private int screenSizeY;

    private Player player;
    private Paint paint;
    private SharedPreferencesManager spm;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private ArrayList<Enemy> enemies;
    private AnimatedBackground background;

    /*
        Drawer initialization
    */
    public Drawer(int screenSizeX, int screenSizeY, Canvas canvas, Paint paint,
                  SurfaceHolder surfaceHolder, SharedPreferencesManager spm) {
        this.surfaceHolder = surfaceHolder;
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        this.paint = paint;
        this.spm = spm;
        this.canvas = canvas;
    }

    /*
        Draws all bitmaps and states
    */
    public void draw(GameStatus currentGameStatus) {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            for (Star s : background.getStars()) {
                canvas.drawBitmap(s.getBitmap(), s.getX(), s.getY(), paint);
            }

            for (Laser l : player.getLasers()) {
                canvas.drawBitmap(l.getBitmap(), l.getX(), l.getY(), paint);
            }

            for (Enemy m : enemies) {
                canvas.drawBitmap(m.getBitmap(), m.getX(), m.getY(), paint);
                drawHealth(m, m.getX(), m.getY(), paint);
            }

            //Draw player
            canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), paint);

            drawScore();

            if (currentGameStatus == GameStatus.GameOver || currentGameStatus == GameStatus.NewHighScore) {
                drawGameOver(currentGameStatus);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /*
        Draws enemies health
    */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawHealth(Enemy enemy, int x, int y, Paint paint) {
        if (!enemy.isDestroyed()) {
            //Shift health drawing point, it is necessary so that
            // regardless of amount of health is always in the middle
            int shift = String.valueOf(enemy.getHealth()).length();
            Bitmap meteorBitmap = enemy.getBitmap();

            setHealthPaintSettings();

            canvas.drawText("" + enemy.getHealth(), x + meteorBitmap.getWidth() / 2 - shift * 10,
                    y + meteorBitmap.getHeight() / 2, paint);
        }
    }

    /*
        Sets settings for drawHealth()
    */
    private void setHealthPaintSettings() {
        paint.setTextSize(40);
        paint.setColor(Color.WHITE);
        paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        paint.setShadowLayer(0.1f, -2, 2, Color.BLACK);
    }

    /*
        Draws score
    */
    private void drawScore() {
        Paint score = new Paint();
        score.setTextSize(30);
        score.setColor(Color.WHITE);

        canvas.drawText("Score : " + SCORE, 100, 50, score);
    }

    /*
        Draws GameOver state
    */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawGameOver(GameStatus currentGameStatus) {
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
        if (currentGameStatus == GameStatus.NewHighScore) {
            canvas.drawText("New High Score : " + spm.getHighScore(),
                    screenSizeX / 2, (screenSizeY / 2) + 120, highScore);

            Paint enemyDestroyed = new Paint();
            enemyDestroyed.setTextSize(50);
            enemyDestroyed.setTextAlign(Paint.Align.CENTER);
            enemyDestroyed.setColor(Color.WHITE);

            canvas.drawText(
                    "EnemyShip Destroyed : " + spm.getEnemyDestroyed(),
                    screenSizeX / 2, (screenSizeY / 2) + 180, enemyDestroyed);

            Paint meteorDestroyed = new Paint();
            meteorDestroyed.setTextSize(50);
            meteorDestroyed.setTextAlign(Paint.Align.CENTER);
            meteorDestroyed.setColor(Color.WHITE);

            canvas.drawText("Meteor Destroyed : " + spm.getMeteorDestroyed(),
                    screenSizeX / 2, (screenSizeY / 2) + 240, meteorDestroyed);
        }
    }

    /*
        SETTERS
    */
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void setBackground(AnimatedBackground background) {
        this.background = background;
    }
}
