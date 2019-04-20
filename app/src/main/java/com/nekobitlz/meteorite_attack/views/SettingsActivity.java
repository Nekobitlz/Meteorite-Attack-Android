package com.nekobitlz.meteorite_attack.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;

/*
    Activity where user can adjust the sound
*/
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox soundStatus;
    private SeekBar soundVolume;
    private ImageView back;

    private SharedPreferencesManager spm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Make the display full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Make the display always turn on if the activity is active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        soundStatus = findViewById(R.id.sound_checkbox);
        soundVolume = findViewById(R.id.sound_seekbar);
        back = findViewById(R.id.back);

        spm = new SharedPreferencesManager(this);

        soundVolume.setProgress(spm.getVolume());
        soundStatus.setChecked(spm.getSoundStatus());

        back.setOnClickListener(this);
        soundVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Progress == volume level
                spm.saveSound(spm.getSoundStatus(), progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                /* NOTHING */
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                /* NOTHING */
            }
        });

        // *Checked* -> Sound On
        // *Unchecked* -> Sound off
        soundStatus.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spm.saveSound(true, spm.getVolume());
                } else {
                    spm.saveSound(false, spm.getVolume());
                }
            }
        });
    }

    /*
        Reads views clicks

        When user presses on back button he returns to Main Menu
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    /*
        Handles pressing "back" button
    */
    @Override
    public void onBackPressed() {
        finish();
    }
}
