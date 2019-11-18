package com.nekobitlz.meteorite_attack.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.options.Shop;
import com.nekobitlz.meteorite_attack.views.fragments.ShopFragment.ShopItem;

import java.util.ArrayList;

import static com.nekobitlz.meteorite_attack.options.Shop.HEALTH;
import static com.nekobitlz.meteorite_attack.options.Shop.MAX;
import static com.nekobitlz.meteorite_attack.options.Shop.WEAPON_POWER;
import static com.nekobitlz.meteorite_attack.options.Shop.XSCORE;

/*
    Fragment that contains the item from the shop
*/
public class ShopItemFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    static final String SAVE_PAGE_NUMBER = "save_page_number";

    private ArrayList<ShopItem> shopItemList;
    private ShopFragment parentFragment;
    private ShopItem shopItem;
    private int pageNumber;
    private int maxValue;

    private ImageView shopImage;
    private TextView levelHealth;
    private TextView levelXScore;
    private TextView levelBullet;
    private Button priceHealth;
    private Button priceBullet;
    private Button priceXScore;
    private Button shipPriceButton;

    private SharedPreferencesManager spm;
    private Shop shop;

    /*
        Creates a new instance of the fragment
    */
    public static ShopItemFragment newInstance(int page) {
        ShopItemFragment shopItemFragment = new ShopItemFragment();
        Bundle arguments = new Bundle();

        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        shopItemFragment.setArguments(arguments);

        return shopItemFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spm = new SharedPreferencesManager(getContext());
        shop = new Shop(getContext());
        pageNumber = getArguments() != null ? getArguments().getInt(ARGUMENT_PAGE_NUMBER) : 0;
        parentFragment = (ShopFragment) getParentFragment();
        shopItemList = parentFragment != null ? parentFragment.getShopItemList() : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_item, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shopItem = shopItemList.get(pageNumber);
        String currentTag = String.valueOf(shopItem.getImage());
        maxValue = (pageNumber + 1) * 5; //formula for calculating the maximum value

        /*
            If the level of upgrade is higher than the maximum
                -> more performance can't be improved and "MAX" is displayed
        */
        String health = getLevel(spm.getHealth(currentTag));
        String weaponPower = getLevel(spm.getWeaponPower(currentTag));
        String xScore = getLevel(spm.getXScore(currentTag));
        String shipPrice = getShipPrice();

        initViews(view);
        initLevelText(health, weaponPower, xScore);
        initButtonsText(health, weaponPower, xScore, shipPrice);
        initButtonsOnClickListeners();

        shopImage.setImageResource(shopItem.getImage());
        // All buttons are added to one map (needed to set "USE" status for items)
        shop.addPriceButtonToMap(currentTag, shipPriceButton);
    }

    private String getLevel(int level) {
        String levelText = String.valueOf(level);
        levelText = level < maxValue ? levelText : MAX;
        return levelText;
    }

    private String getShipPrice() {
        String shopStatus = String.valueOf(shopItem.getImage());
        return spm.getStatus(shopStatus);
    }

    private void initViews(@NonNull View view) {
        shopImage = view.findViewById(R.id.iv_ship);
        levelHealth = view.findViewById(R.id.tv_health);
        levelXScore = view.findViewById(R.id.tv_xscore);
        levelBullet = view.findViewById(R.id.tv_bullet);
        priceHealth = view.findViewById(R.id.btn_price_health);
        priceBullet = view.findViewById(R.id.btn_price_bullet);
        priceXScore = view.findViewById(R.id.btn_price_xscore);
        shipPriceButton = view.findViewById(R.id.btn_ship_price);
    }

    private void initLevelText(String health, String weaponPower, String xScore) {
        levelHealth.setText(health);
        levelBullet.setText(weaponPower);
        levelXScore.setText(xScore);
    }

    private void initButtonsText(String health, String weaponPower, String xScore, String shipPrice) {
        priceHealth.setText(health.equals(MAX) ? "" : shopItem.getUpgradePrice());
        priceBullet.setText(weaponPower.equals(MAX) ? "" : shopItem.getUpgradePrice());
        priceXScore.setText(xScore.equals(MAX) ? "" : shopItem.getUpgradePrice());
        shipPriceButton.setText(shipPrice);
    }

    private void initButtonsOnClickListeners() {
        priceHealth.setOnClickListener(handleUpgradeButton(HEALTH, priceHealth, levelHealth));
        priceBullet.setOnClickListener(handleUpgradeButton(WEAPON_POWER, priceBullet, levelBullet));
        priceXScore.setOnClickListener(handleUpgradeButton(XSCORE, priceXScore, levelXScore));
        shipPriceButton.setOnClickListener(handleBuyShipButton());
    }

    private View.OnClickListener handleUpgradeButton(final String upgradeName, final Button priceButton, final TextView levelView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop.processUpgrade(shopItem, upgradeName, priceButton, levelView, maxValue);
                parentFragment.loadMoney();
            }
        };
    }

    private View.OnClickListener handleBuyShipButton() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop.processShip(shopItem, shipPriceButton);
                parentFragment.loadMoney();
            }
        };
    }

    /*
            Saves the last open item

            * NOT WORKING NOW  ¯\_(ツ)_/¯ *
    */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_PAGE_NUMBER, pageNumber);
    }
}
