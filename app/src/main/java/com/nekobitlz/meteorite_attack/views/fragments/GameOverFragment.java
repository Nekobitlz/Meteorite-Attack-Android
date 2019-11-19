package com.nekobitlz.meteorite_attack.views.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

    private String meteorDestroyed;
    private String money;
    private String enemyShipDestroyed;
    private String borderDestroyed;
    private String exploderDestroyed;

    private TextView moneyCount;
    private TextView meteorCount;
    private TextView enemyShipCount;
    private TextView borderCount;
    private TextView exploderCount;
    private ConstraintLayout constraintLayout;

    public static GameOverFragment newInstance() {
        return new GameOverFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRoundedStyle();

        View view = inflater.inflate(R.layout.fragment_game_over, container, false);

        initViews(view);
        initViewsText();

        constraintLayout.setOnClickListener(closeActivity());

        return view;
    }

    private void initArguments() {
        money = String.valueOf(GameView.MONEY);
        meteorDestroyed = String.valueOf(GameView.METEOR_DESTROYED);
        enemyShipDestroyed = String.valueOf(GameView.ENEMY_SHIP_DESTROYED);
        borderDestroyed = String.valueOf(GameView.BORDER_DESTROYER_DESTROYED);
        exploderDestroyed = String.valueOf(GameView.EXPLODER_DESTROYED);
    }

    private void setRoundedStyle() {
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void initViews(View view) {
        moneyCount = view.findViewById(R.id.tv_money);
        meteorCount = view.findViewById(R.id.tv_meteor_count);
        enemyShipCount = view.findViewById(R.id.tv_enemy_ship_count);
        borderCount = view.findViewById(R.id.tv_border_count);
        exploderCount = view.findViewById(R.id.tv_exploder_count);
        constraintLayout = view.findViewById(R.id.container_game_over);
    }

    private void initViewsText() {
        moneyCount.setText(formatMoney(money));
        meteorCount.setText(meteorDestroyed);
        enemyShipCount.setText(enemyShipDestroyed);
        borderCount.setText(borderDestroyed);
        exploderCount.setText(exploderDestroyed);
    }

    private View.OnClickListener closeActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) getActivity().finish();
            }
        };
    }
}
