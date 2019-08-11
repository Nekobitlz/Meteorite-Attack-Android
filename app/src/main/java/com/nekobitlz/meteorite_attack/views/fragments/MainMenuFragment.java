package com.nekobitlz.meteorite_attack.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.views.activities.MainActivity;
import com.nekobitlz.meteorite_attack.views.activities.MainMenuActivity;
import com.nekobitlz.meteorite_attack.views.activities.SettingsActivity;
import com.nekobitlz.meteorite_attack.views.activities.ShopActivity;

import static com.nekobitlz.meteorite_attack.views.activities.MainMenuActivity.backgroundSound;

public class MainMenuFragment extends Fragment implements View.OnClickListener {

    public static final String BACK_STACK = "BACK_STACK";

    private Button play;
    private Button exit;
    private ImageButton shop;
    private ImageButton highScore;
    private ImageButton settings;
    private TextView money;

    private FragmentManager fragmentManager;

    private SharedPreferencesManager spm;

    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        play = view.findViewById(R.id.play);
        highScore = view.findViewById(R.id.high_score);
        exit = view.findViewById(R.id.exit);
        money = view.findViewById(R.id.money);
        shop = view.findViewById(R.id.shop);
        settings = view.findViewById(R.id.settings);

        play.setOnClickListener(this);
        shop.setOnClickListener(this);
        highScore.setOnClickListener(this);
        exit.setOnClickListener(this);
        settings.setOnClickListener(this);

        fragmentManager = getActivity().getSupportFragmentManager();

        spm = new SharedPreferencesManager(getContext());
        loadMoney();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMoney();
    }

    public void loadMoney() {
        money.setText(String.valueOf(spm.getMoney()));
    }

    /*
           Reads views clicks
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play: {
                startActivity(new Intent(getContext(), MainActivity.class));
                ((MainMenuActivity) getActivity()).setToOtherActivity(true);
            }
            break;

            case R.id.shop: {
                startActivity(new Intent(getContext(), ShopActivity.class));
                ((MainMenuActivity) getActivity()).setToOtherActivity(true);
            }
            break;

            case R.id.high_score: {
                HighScoreFragment fragment = HighScoreFragment.newInstance();

                fragmentManager.beginTransaction()
                        .addToBackStack(BACK_STACK)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.content, fragment)
                        .commit();
            }
            break;

            case R.id.settings: {
                startActivity(new Intent(getContext(), SettingsActivity.class));
                ((MainMenuActivity) getActivity()).setToOtherActivity(true);
            }
            break;

            case R.id.exit: {
                if (backgroundSound != null) {
                    backgroundSound.releaseMP();
                }

                getActivity().finish();
            }
            break;
        }
    }

}
