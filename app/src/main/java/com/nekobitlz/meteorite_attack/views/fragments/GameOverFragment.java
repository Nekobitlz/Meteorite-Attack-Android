package com.nekobitlz.meteorite_attack.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.views.GameView;

import static com.nekobitlz.meteorite_attack.options.Utils.formatMoney;

/*
    Dialog with game over:

            GAME OVER
            * Money
        Meteor :     count
        EnemyShip :  count
        Exploder :   count
        Border :     count
*/
public class GameOverFragment extends DialogFragment {

    private String score;
    private String meteorDestroyed;
    private String money;
    private String enemyShipDestroyed;
    private String borderDestroyed;
    private String exploderDestroyed;

    public static GameOverFragment newInstance() {
        return new GameOverFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        score = String.valueOf(GameView.SCORE);
        money = String.valueOf(GameView.MONEY);
        meteorDestroyed = String.valueOf(GameView.METEOR_DESTROYED);
        enemyShipDestroyed = String.valueOf(GameView.ENEMY_SHIP_DESTROYED);
        borderDestroyed = String.valueOf(GameView.BORDER_DESTROYER_DESTROYED);
        exploderDestroyed = String.valueOf(GameView.EXPLODER_DESTROYED);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_over, container, false);

        TextView moneyCount = view.findViewById(R.id.money);
        moneyCount.setText(formatMoney(money));

        TextView meteorCount = view.findViewById(R.id.meteor_count);
        meteorCount.setText(meteorDestroyed);

        TextView enemyShipCount = view.findViewById(R.id.enemy_ship_count);
        enemyShipCount.setText(enemyShipDestroyed);

        TextView borderCount = view.findViewById(R.id.border_count);
        borderCount.setText(borderDestroyed);

        TextView exploderCount = view.findViewById(R.id.exploder_count);
        exploderCount.setText(exploderDestroyed);

        ConstraintLayout constraintLayout = view.findViewById(R.id.game_over_container);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }
}
