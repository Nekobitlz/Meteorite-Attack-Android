package com.nekobitlz.meteorite_attack.options;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import com.nekobitlz.meteorite_attack.enums.GameStatus;
import com.nekobitlz.meteorite_attack.objects.*;

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

    private ArrayList<Meteorite> meteors;
    private ArrayList<EnemyShip> enemyShips;
    private ArrayList<BorderDestroyerMeteor> borderDestroyerMeteors;
    private ArrayList<ExploderMeteor> exploderMeteors;
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

            for (Meteorite m : meteors) {
                canvas.drawBitmap(m.getBitmap(), m.getX(), m.getY(), paint);
                drawHealth(m, m.getX(), m.getY(), paint);
            }

            for (EnemyShip e : enemyShips) {
                canvas.drawBitmap(e.getBitmap(), e.getX(), e.getY(), paint);
                drawHealth(e, e.getX(), e.getY(), paint);
            }

            for (BorderDestroyerMeteor bdm : borderDestroyerMeteors) {
                canvas.drawBitmap(bdm.getBitmap(), bdm.getX(), bdm.getY(), paint);
                drawHealth(bdm, bdm.getX(), bdm.getY(), paint);
            }

            for (ExploderMeteor exploder: exploderMeteors) {
                canvas.drawBitmap(exploder.getBitmap(), exploder.getX(), exploder.getY(), paint);
                drawHealth(exploder, exploder.getX(), exploder.getY(), paint);
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
        Draws exploders health
    */
    private void drawHealth(ExploderMeteor exploder, int x, int y, Paint paint) {
        if (exploder.getHealth() > 0) {
            //Shift health drawing point, it is necessary so that
            // regardless of amount of health is always in the middle
            int shift = String.valueOf(exploder.getHealth()).length();
            Bitmap exploderBitmap = exploder.getBitmap();

            setHealthPaintSettings();

            canvas.drawText("" + exploder.getHealth(), x + exploderBitmap.getWidth() / 2 - shift * 10,
                    y + exploderBitmap.getHeight() / 2, paint);
        }
    }

    /*
        Draws meteors health
    */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawHealth(Meteorite meteor, int x, int y, Paint paint) {
        if (meteor.getHealth() > 0) {
            //Shift health drawing point, it is necessary so that
            // regardless of amount of health is always in the middle
            int shift = String.valueOf(meteor.getHealth()).length();
            Bitmap meteorBitmap = meteor.getBitmap();

            setHealthPaintSettings();

            canvas.drawText("" + meteor.getHealth(), x + meteorBitmap.getWidth() / 2 - shift * 10,
                    y + meteorBitmap.getHeight() / 2, paint);
        }
    }

    /*
        Draws enemy ships health
    */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawHealth(EnemyShip enemyShip, int x, int y, Paint paint) {
        if (enemyShip.getHealth() > 0) {
            //Shift health drawing point, it is necessary so that
            // regardless of amount of health is always in the middle
            int shift = String.valueOf(enemyShip.getHealth()).length();
            Bitmap enemyShipBitmap = enemyShip.getBitmap();

            setHealthPaintSettings();

            canvas.drawText("" + enemyShip.getHealth(), x + enemyShipBitmap.getWidth() / 2 - shift * 10,
                    y + enemyShipBitmap.getHeight() / 2, paint);
        }
    }

    /*
        Draws "border destroyer" meteors health
    */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawHealth(BorderDestroyerMeteor bdm, int x, int y, Paint paint) {
        if (bdm.getHealth() > 0) {
            //Shift health drawing point, it is necessary so that
            // regardless of amount of health is always in the middle
            int shift = String.valueOf(bdm.getHealth()).length();
            Bitmap meteorBitmap = bdm.getBitmap();

            setHealthPaintSettings();

            canvas.drawText("" + bdm.getHealth(), x + meteorBitmap.getWidth() / 2 - shift * 10,
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

    public void setMeteors(ArrayList<Meteorite> meteors) {
        this.meteors = meteors;
    }

    public void setEnemyShips(ArrayList<EnemyShip> enemyShips) {
        this.enemyShips = enemyShips;
    }

    public void setBackground(AnimatedBackground background) {
        this.background = background;
    }

    public void setBorderDestroyers(ArrayList<BorderDestroyerMeteor> borderDestroyerMeteors) {
        this.borderDestroyerMeteors = borderDestroyerMeteors;
    }

    public void setExploders(ArrayList<ExploderMeteor> exploderMeteors) {
        this.exploderMeteors = exploderMeteors;
    }
}
