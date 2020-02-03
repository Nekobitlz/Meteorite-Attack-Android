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
import com.nekobitlz.meteorite_attack.views.custom.ClickShrinkEffect;

import static com.nekobitlz.meteorite_attack.options.Utils.formatMoney;
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
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentManager = getActivity() != null ? getActivity().getSupportFragmentManager() : null;
        spm = new SharedPreferencesManager(getContext());

        initViews(view);
        initOnClickListeners();
        initClickShrinkEffect();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMoney();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play: {
                if (getActivity() != null) {
                    ((MainMenuActivity) getActivity()).setToOtherActivity(true);
                }
                startActivity(new Intent(getContext(), MainActivity.class));
            }
            break;

            case R.id.ib_shop: {
                ShopFragment fragment = ShopFragment.newInstance();
                replaceFragment(fragment);
            }
            break;

            case R.id.ib_high_score: {
                HighScoreFragment fragment = HighScoreFragment.newInstance();
                replaceFragment(fragment);
            }
            break;

            case R.id.ib_settings: {
                SettingsFragment fragment = SettingsFragment.newInstance();
                replaceFragment(fragment);
            }
            break;

            case R.id.btn_exit: {
                if (backgroundSound != null) {
                    backgroundSound.releaseMP();
                }
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
            break;
        }
    }

    private void initViews(@NonNull View view) {
        play = view.findViewById(R.id.btn_play);
        highScore = view.findViewById(R.id.ib_high_score);
        exit = view.findViewById(R.id.btn_exit);
        money = view.findViewById(R.id.tv_money);
        shop = view.findViewById(R.id.ib_shop);
        settings = view.findViewById(R.id.ib_settings);
    }

    private void initOnClickListeners() {
        play.setOnClickListener(this);
        shop.setOnClickListener(this);
        highScore.setOnClickListener(this);
        exit.setOnClickListener(this);
        settings.setOnClickListener(this);
    }

    private void initClickShrinkEffect() {
        ClickShrinkEffect.applyClickShrink(play);
        ClickShrinkEffect.applyClickShrink(shop);
        ClickShrinkEffect.applyClickShrink(highScore);
        ClickShrinkEffect.applyClickShrink(settings);
        ClickShrinkEffect.applyClickShrink(exit);
    }

    public void loadMoney() {
        String moneyText = String.valueOf(spm.getMoney());
        money.setText(formatMoney(moneyText));
    }

    private void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .addToBackStack(BACK_STACK)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.content, fragment)
                .commit();
    }
}
