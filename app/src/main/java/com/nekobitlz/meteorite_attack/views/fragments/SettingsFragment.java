package com.nekobitlz.meteorite_attack.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.services.MusicService;
import com.nekobitlz.meteorite_attack.views.activities.MainMenuActivity;

public class SettingsFragment extends Fragment {

    private CheckBox soundStatus;
    private SeekBar effectsVolume;
    private SeekBar musicVolume;
    private ImageView back;

    private SharedPreferencesManager spm;
    private MusicService musicService;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spm = new SharedPreferencesManager(getContext());
        musicService = getActivity() == null ? null : ((MainMenuActivity) getActivity()).getMusicService();

        initViews(view);
        initProgress();

        back.setOnClickListener(closeActivity());
        effectsVolume.setOnSeekBarChangeListener(saveEffectsVolume());
        musicVolume.setOnSeekBarChangeListener(saveMusicVolume());
        soundStatus.setOnCheckedChangeListener(handleOnChecked());

    }

    private void initViews(@NonNull View view) {
        soundStatus = view.findViewById(R.id.cb_sound_status);
        effectsVolume = view.findViewById(R.id.sb_effects_volume);
        musicVolume = view.findViewById(R.id.sb_music_volume);
        back = view.findViewById(R.id.iv_back_btn);
    }

    private void initProgress() {
        effectsVolume.setProgress(spm.getEffectsVolume());
        musicVolume.setProgress(spm.getMusicVolume());
        soundStatus.setChecked(spm.getSoundStatus());
    }

    private View.OnClickListener closeActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) getActivity().onBackPressed();
            }
        };
    }

    private SeekBar.OnSeekBarChangeListener saveEffectsVolume() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Progress == volume level
                spm.saveSound(spm.getSoundStatus(), progress, spm.getMusicVolume());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                /* NOTHING */
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                /* NOTHING */
            }
        };
    }

    private SeekBar.OnSeekBarChangeListener saveMusicVolume() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Progress == volume level
                //        float volume = (float) (1 - (Math.log(100 - progress) / Math.log(100)));
                spm.saveSound(spm.getSoundStatus(), spm.getEffectsVolume(), progress);
                musicService.setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                /* NOTHING */
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                /* NOTHING */
            }
        };
    }

    private CheckBox.OnCheckedChangeListener handleOnChecked() {
        return new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spm.saveSound(true, spm.getEffectsVolume(), spm.getMusicVolume());
                    musicService.setEnabled(true);
                } else {
                    spm.saveSound(false, spm.getEffectsVolume(), spm.getMusicVolume());
                    musicService.setEnabled(false);
                }
            }
        };
    }
}