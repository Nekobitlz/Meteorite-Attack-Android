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

import static com.nekobitlz.meteorite_attack.options.Utils.formatMoney;

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
        String moneyText = String.valueOf(spm.getMoney());
        money.setText(formatMoney(moneyText));
    }

    /*
           Reads views clicks
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play: {
                ((MainMenuActivity) getActivity()).setToOtherActivity(true);
                startActivity(new Intent(getContext(), MainActivity.class));
            }
            break;

            case R.id.shop: {
                ShopFragment fragment = ShopFragment.newInstance();

                fragmentManager.beginTransaction()
                        .addToBackStack(BACK_STACK)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.content, fragment)
                        .commit();
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
                SettingsFragment fragment = SettingsFragment.newInstance();

                fragmentManager.beginTransaction()
                        .addToBackStack(BACK_STACK)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.content, fragment)
                        .commit();
            }
            break;

            case R.id.exit: {
                getActivity().finish();
            }
            break;
        }
    }
}
