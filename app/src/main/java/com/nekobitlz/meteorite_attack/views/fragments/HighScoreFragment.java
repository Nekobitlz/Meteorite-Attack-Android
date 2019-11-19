package com.nekobitlz.meteorite_attack.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;

public class HighScoreFragment extends Fragment {

    private TextView score;
    private TextView meteor;
    private TextView enemyShip;
    private TextView borderDestroyer;
    private TextView exploder;
    private ImageView back;

    public static HighScoreFragment newInstance() {
        return new HighScoreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_high_score, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        loadHighScore();

        back.setOnClickListener(closeFragment());
    }

    private void initViews(@NonNull View view) {
        score = view.findViewById(R.id.tv_score_count);
        meteor = view.findViewById(R.id.tv_meteor_count);
        enemyShip = view.findViewById(R.id.tv_enemy_ship_count);
        borderDestroyer = view.findViewById(R.id.tv_border_count);
        exploder = view.findViewById(R.id.tv_exploder_count);
        back = view.findViewById(R.id.iv_back_btn);
    }

    private View.OnClickListener closeFragment() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) getActivity().onBackPressed();
            }
        };
    }

    private void loadHighScore() {
        SharedPreferencesManager spm = new SharedPreferencesManager(getContext());

        score.setText(String.valueOf(spm.getHighScore()));
        meteor.setText(String.valueOf(spm.getMeteorDestroyed()));
        enemyShip.setText(String.valueOf(spm.getEnemyDestroyed()));
        borderDestroyer.setText(String.valueOf(spm.getBorderDestroyed()));
        exploder.setText(String.valueOf(spm.getExploderDestroyed()));
    }
}
