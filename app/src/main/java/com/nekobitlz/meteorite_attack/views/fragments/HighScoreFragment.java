package com.nekobitlz.meteorite_attack.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;

public class HighScoreFragment extends Fragment {

    private TextView score;
    private TextView meteor;
    private TextView enemyShip;
    private TextView borderDestroyer;
    private TextView exploder;
    private TextView nullHighScore;
    private ImageView back;

    private ConstraintLayout highScoreContainer;

    public static HighScoreFragment newInstance() {
        return new HighScoreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_high_score, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        score = view.findViewById(R.id.score_count);
        meteor = view.findViewById(R.id.meteor_count);
        enemyShip = view.findViewById(R.id.enemy_ship_count);
        borderDestroyer = view.findViewById(R.id.border_count);
        exploder = view.findViewById(R.id.exploder_count);
        nullHighScore = view.findViewById(R.id.null_high_score);
        highScoreContainer = view.findViewById(R.id.high_score_container);
        back = view.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        loadHighScore();
    }

    /*
        Loads your saved high score
    */
    private void loadHighScore() {
        SharedPreferencesManager spm = new SharedPreferencesManager(getContext());

        if (spm.getHighScore() != -1) {
            nullHighScore.setVisibility(TextView.GONE);
            highScoreContainer.setVisibility(LinearLayout.VISIBLE);

            score.setText(String.valueOf(spm.getHighScore()));
            meteor.setText(String.valueOf(spm.getMeteorDestroyed()));
            enemyShip.setText(String.valueOf(spm.getEnemyDestroyed()));
            borderDestroyer.setText(String.valueOf(spm.getBorderDestroyed()));
            exploder.setText(String.valueOf(spm.getExploderDestroyed()));
        } else {
            nullHighScore.setVisibility(TextView.VISIBLE);
            highScoreContainer.setVisibility(LinearLayout.GONE);
        }
    }
}
