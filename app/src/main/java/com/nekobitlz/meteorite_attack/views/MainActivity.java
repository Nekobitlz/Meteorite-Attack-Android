package com.nekobitlz.meteorite_attack.views;

import android.content.Context;
import android.graphics.Point;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;

import com.nekobitlz.meteorite_attack.enums.Direction;

/*
    Activity with game
*/
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private GameView gameView;

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

        gameView = new GameView(this, point.x, point.y);
        setContentView(gameView);

        //The accelerometer sensor is used to move the player to the right and left
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    /*
        Resumes the game
    */
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    /*
        Pauses the game
    */
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    /*
        Reads acceleration changes
    */
    @Override
    public void onSensorChanged(SensorEvent event) {
        float xTemp = event.values[0];

        if (event.values[0] > 1)
            gameView.setDirection(Direction.Left, xTemp);
        else if (event.values[0] < -1)
            gameView.setDirection(Direction.Right, xTemp);
        else
            gameView.setDirection(Direction.Stopped, 0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //TODO()
    }

    /*
        Reads back button click
    */
    @Override
    public void onBackPressed() {
        gameView.setMainMenuActivity();
    }
}

