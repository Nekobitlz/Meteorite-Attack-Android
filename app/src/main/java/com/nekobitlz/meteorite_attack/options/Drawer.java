package com.nekobitlz.meteorite_attack.options;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.view.SurfaceHolder;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.enums.EnemyType;
import com.nekobitlz.meteorite_attack.objects.Bonus;
import com.nekobitlz.meteorite_attack.objects.Laser;
import com.nekobitlz.meteorite_attack.objects.Player;
import com.nekobitlz.meteorite_attack.objects.Star;
import com.nekobitlz.meteorite_attack.objects.enemies.Enemy;

import java.util.ArrayList;

import static com.nekobitlz.meteorite_attack.views.GameView.BONUS_DURATION;
import static com.nekobitlz.meteorite_attack.views.GameView.SCORE;

/*
     Draws all objects and game states
*/
public class Drawer {

    private Player player;
    private Paint paint;
    private Paint alphaPaint;
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
    public Drawer(Context context,
                  SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;

        typeface = ResourcesCompat.getFont(context, R.font.iceberg_regular);
        paint = new Paint();
        paint.setTypeface(typeface);

        alphaPaint = new Paint();
        // Filter for smooth reduction of alpha
        alphaPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));

        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_money);
        coinBitmap = Bitmap.createScaledBitmap(
                coinBitmap, coinBitmap.getWidth() / 10, coinBitmap.getHeight() / 10, false);
    }

    public Drawer(SurfaceHolder surfaceHolder, AnimatedBackground animatedBackground) {
        this.surfaceHolder = surfaceHolder;
        this.background = animatedBackground;

        paint = new Paint();
    }

    /*
        Draws all bitmaps and states
    */
    public void draw() {
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

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void drawBackground() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            for (Star s : background.getStars()) {
                canvas.drawBitmap(s.getBitmap(), s.getX(), s.getY(), paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /*
        Draws enemies health
    */
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
        score.setTextSize(40);
        score.setColor(Color.WHITE);
        score.setTypeface(typeface);

        canvas.drawText("Score : " + SCORE, 100, 50, score);
    }

    /*
        Draws bonus duration

        If bonus is on -> it draws duration
        otherwise -> removes it from the screen
    */
    private void drawBonusDuration() {
        Paint bonusDuration = new Paint();
        bonusDuration.setTextSize(40);
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
