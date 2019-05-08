package com.nekobitlz.meteorite_attack.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.nekobitlz.meteorite_attack.R;

/*
    Dialog with game over:

       GAME OVER
        * Money
       Meteor : count
       EnemyShip : count
       Exploder : count
       Border : count
*/
public class GameOverFragment extends DialogFragment {
    private GameView gameView;

    private String score;
    private String meteorDestroyed;
    private String money;
    private String enemyShipDestroyed;
    private String borderDestroyed;
    private String exploderDestroyed;

    public GameOverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        gameView = ((MainActivity) activity).getSurfaceView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        score = GameView.SCORE + "";
        money = GameView.MONEY + "";
        meteorDestroyed = GameView.METEOR_DESTROYED + "";
        enemyShipDestroyed = GameView.ENEMY_SHIP_DESTROYED + "";
        borderDestroyed = GameView.BORDER_DESTROYER_DESTROYED + "";
        exploderDestroyed = GameView.EXPLODER_DESTROYED + "";
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_game_over, null);

        TextView moneyCount = view.findViewById(R.id.money);
        moneyCount.setText(money);

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

        final Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(false)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

                dialog.getWindow().setAttributes(params);
            }
        });

        return dialog;
    }
}
