package com.nekobitlz.meteorite_attack.options;

import android.content.Context;
import android.graphics.*;
import android.support.v4.content.res.ResourcesCompat;
import android.view.SurfaceHolder;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.enums.EnemyType;
import com.nekobitlz.meteorite_attack.enums.GameStatus;
import com.nekobitlz.meteorite_attack.objects.Bonus;
import com.nekobitlz.meteorite_attack.objects.Laser;
import com.nekobitlz.meteorite_attack.objects.Player;
import com.nekobitlz.meteorite_attack.objects.Star;
import com.nekobitlz.meteorite_attack.objects.enemies.Enemy;

import java.util.ArrayList;

import static com.nekobitlz.meteorite_attack.views.GameView.*;

/*
     Draws all objects and game states
*/
public class Drawer {
    private int screenSizeX;
    private int screenSizeY;

    private Player player;
    private Paint paint;
    private Paint alphaPaint;
    private SharedPreferencesManager spm;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Bitmap coinBitmap;

    private ArrayList<Enemy> enemies;
    private ArrayList<Bonus> bonuses;
    private AnimatedBackground background;

    private Typeface typeface;

    /*
        Drawer initialization
    */
    public Drawer(Context context, int screenSizeX, int screenSizeY, Canvas canvas, Paint paint,
                  SurfaceHolder surfaceHolder, SharedPreferencesManager spm) {
        this.surfaceHolder = surfaceHolder;
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        this.paint = paint;
        this.spm = spm;
        this.canvas = canvas;

        typeface = ResourcesCompat.getFont(context, R.font.iceberg_regular);
        paint.setTypeface(typeface);

        alphaPaint = new Paint();
        // Filter for smooth reduction of alpha
        alphaPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));

        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_money);
        coinBitmap = Bitmap.createScaledBitmap(
                coinBitmap, coinBitmap.getWidth() / 10, coinBitmap.getHeight() / 10, false);
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

            for (Bonus b : bonuses) {
                canvas.drawBitmap(b.getBitmap(), b.getX(), b.getY(), paint);
            }

            for (Enemy e : enemies) {
                if (e.isDestroyed() && e.getAlpha() > 0 && e.getEnemyType() != EnemyType.Exploder) {
                    // Smooth disappearance of an explosion
                    int alpha = e.getAlpha() - 15;

                    e.setAlpha(alpha);
                    alphaPaint.setAlpha(alpha);

                    canvas.drawBitmap(e.getBitmap(), e.getX(), e.getY(), alphaPaint);
                } else {
                    if (!e.isDestroyed() || e.getEnemyType() == EnemyType.Exploder) {
                        canvas.drawBitmap(e.getBitmap(), e.getX(), e.getY(), paint);
                        drawHealth(e, e.getX(), e.getY(), paint);
                    }
                }
            }

            for (Laser l : player.getLasers()) {
                canvas.drawBitmap(l.getBitmap(), l.getX(), l.getY(), paint);
            }

            //Draw player
            canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), paint);
            drawHealth(player, player.getX(), player.getY(), paint);

            drawScore();
            drawBonusDuration();

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
            Bitmap enemyBitmap = enemy.getBitmap();
            int textSize = enemyBitmap.getWidth() * 2 / 5;
            int textLength = String.valueOf(enemy.getHealth()).length();

            // Border has a slightly different height, need to shift inscription to center
            int shiftY = enemy.getEnemyType() == EnemyType.BorderDestroyer ? 10 : 5;
            int healthY = y + textSize + enemyBitmap.getHeight() / shiftY;
            int healthX = (x + enemyBitmap.getWidth() / 2) - (textSize * textLength) / 4;

            paint.setTextSize(textSize);
            paint.setColor(Color.WHITE);
            paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
            paint.setShadowLayer(0.1f, -2, 2, Color.BLACK);

            canvas.drawText("" + enemy.getHealth(), healthX, healthY, paint);
        }
    }

    /*
       Draws player health
   */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawHealth(Player player, float x, float y, Paint paint) {
        if (player.getHealth() > 0) {
            //Shift health drawing point, it is necessary so that
            // regardless of amount of health is always in the middle
            Bitmap playerBitmap = player.getBitmap();
            int shift = String.valueOf(player.getHealth()).length() * 10;
            int textSize = 40;

            paint.setTextSize(textSize);
            paint.setColor(Color.WHITE);
            paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
            paint.setShadowLayer(0.1f, -2, 2, Color.BLACK);

            canvas.drawText("" + player.getHealth(), x + playerBitmap.getWidth() / 2 - shift,
                    y + playerBitmap.getHeight() / 2 + textSize / 3, paint);
        }
    }

    /*
        Draws score
    */
    private void drawScore() {
        Paint score = new Paint();
        score.setTextSize(30);
        score.setColor(Color.WHITE);
        score.setTypeface(typeface);

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
        gameOver.setTypeface(typeface);

        canvas.drawText("GAME OVER", screenSizeX / 2, screenSizeY / 2, gameOver);

        Paint money = new Paint();
        money.setTextSize(55);
        money.setTextAlign(Paint.Align.LEFT);
        money.setColor(Color.WHITE);
        money.setTypeface(typeface);

        // *coin* money
        canvas.drawBitmap(coinBitmap, screenSizeX / 2 - coinBitmap.getWidth(),
                                    (screenSizeY / 2) + 60 - coinBitmap.getHeight() * 4 / 5, money);
        canvas.drawText("" + MONEY, screenSizeX / 2 + coinBitmap.getWidth() / 5, (screenSizeY / 2) + 65, money);

        //Draw high score
        Paint highScore = new Paint();
        highScore.setTextSize(50);
        highScore.setTextAlign(Paint.Align.CENTER);
        highScore.setColor(Color.WHITE);
        highScore.setTypeface(typeface);

        //Draw new high score and stat
        if (currentGameStatus == GameStatus.NewHighScore) {
            canvas.drawText("New High Score : " + spm.getHighScore(),
                    screenSizeX / 2, (screenSizeY / 2) + 120, highScore);

            Paint enemyDestroyed = new Paint();
            enemyDestroyed.setTextSize(50);
            enemyDestroyed.setTextAlign(Paint.Align.CENTER);
            enemyDestroyed.setColor(Color.WHITE);
            enemyDestroyed.setTypeface(typeface);

            canvas.drawText(
                    "EnemyShip Destroyed : " + spm.getEnemyDestroyed(),
                    screenSizeX / 2, (screenSizeY / 2) + 180, enemyDestroyed);

            canvas.drawText("Meteor Destroyed : " + spm.getMeteorDestroyed(),
                    screenSizeX / 2, (screenSizeY / 2) + 240, enemyDestroyed);

            canvas.drawText("Border Destroyer Destroyed : " + spm.getBorderDestroyed(),
                    screenSizeX / 2, (screenSizeY / 2) + 300, enemyDestroyed);

            canvas.drawText("Exploder Destroyed : " + spm.getExploderDestroyed(),
                    screenSizeX / 2, (screenSizeY / 2) + 360, enemyDestroyed);
        }
    }

    /*
        Draws bonus duration

        If bonus is on -> it draws duration
        otherwise -> removes it from the screen
    */
    @SuppressWarnings("deprecation")
    private void drawBonusDuration() {
        Paint bonusDuration = new Paint();
        bonusDuration.setTextSize(30);
        bonusDuration.setColor(Color.WHITE);
        bonusDuration.setTypeface(typeface);

        if (BONUS_DURATION > 0) {
            canvas.drawText("BONUS DURATION : " + (500 - BONUS_DURATION), 100, 90, bonusDuration);
        } else {
            bonusDuration.reset();
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

    public void setBonuses(ArrayList<Bonus> bonuses) {
        this.bonuses = bonuses;
    }
}
