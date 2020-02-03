package com.nekobitlz.meteorite_attack.views.activities;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.nekobitlz.meteorite_attack.views.GameView;

/*
    Activity with game
*/
public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private long backPressed;
    public static MainMenuActivity.BackgroundSound backgroundSound;
    private boolean isStopped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Make the display full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Make the display always turn on if the activity is active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Get screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        backgroundSound = MainMenuActivity.backgroundSound;

        gameView = new GameView(this, point.x, point.y);
        setContentView(gameView);
    }

    /*
        Resumes the game
    */
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();

        if (isStopped) {
            isStopped = false;

            if (backgroundSound != null) {
                backgroundSound.getPlayer().start();
            }
        }
    }

    /*
        Pauses the game
    */
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
        isStopped = true;

        if (backgroundSound != null && backgroundSound.getPlayer() != null) {
            backgroundSound.getPlayer().pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameView.getSoundPlayer().release();
        if (isStopped) {
            isStopped = false;

            if (backgroundSound != null) {
                backgroundSound.getPlayer().start();
            }
        }
    }

    /*
        Exit to MainMenu by double clicking on the Back button
    */
    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis())
            gameView.setMainMenuActivity();
        else
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();

        backPressed = System.currentTimeMillis();
    }

    public GameView getSurfaceView() {
        return gameView;
    }
}