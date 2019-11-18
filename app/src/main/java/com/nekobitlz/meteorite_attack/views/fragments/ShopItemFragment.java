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
        String health = String.valueOf(spm.getHealth(currentTag));
        health = Integer.parseInt(health) < maxValue ? health : "MAX";

        String weaponPower = String.valueOf(spm.getWeaponPower(currentTag));
        weaponPower = Integer.parseInt(weaponPower) < maxValue ? weaponPower : "MAX";

        String xScore = String.valueOf(spm.getXScore(currentTag));
        xScore = Integer.parseInt(xScore) < maxValue ? xScore : "MAX";

        // Setting all the characteristics on the view
        ImageView shopImage = view.findViewById(R.id.iv_ship);
        shopImage.setImageResource(shopItem.getImage());

        final TextView levelHealth = view.findViewById(R.id.tv_health);
        levelHealth.setText(health);

        final TextView levelBullet = view.findViewById(R.id.tv_bullet);
        levelBullet.setText(weaponPower);

        final TextView levelXScore = view.findViewById(R.id.tv_xscore);
        levelXScore.setText(xScore);

        final Button priceHealth = view.findViewById(R.id.btn_price_health);
        priceHealth.setText(health.equals("MAX") ? "" : shopItem.getUpgradePrice());
        priceHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop.processUpgrade(shopItem, "health", priceHealth, levelHealth, maxValue);
                parentFragment.loadMoney();
            }
        });

        final Button priceBullet = view.findViewById(R.id.btn_price_bullet);
        priceBullet.setText(weaponPower.equals("MAX") ? "" : shopItem.getUpgradePrice());
        priceBullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop.processUpgrade(shopItem, "weapon power", priceBullet, levelBullet, maxValue);
                parentFragment.loadMoney();
            }
        });

        final Button priceXScore = view.findViewById(R.id.btn_price_xscore);
        priceXScore.setText(xScore.equals("MAX") ? "" : shopItem.getUpgradePrice());
        priceXScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop.processUpgrade(shopItem, "score multiplier", priceXScore, levelXScore, maxValue);
                parentFragment.loadMoney();
            }
        });

        String shopStatus = String.valueOf(shopItem.getImage());
        String shipPrice = spm.getStatus(shopStatus);

        final Button shipPriceButton = view.findViewById(R.id.btn_ship_price);
        shipPriceButton.setText(shipPrice);
        shipPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop.processShip(shopItem, shipPriceButton);
                parentFragment.loadMoney();
            }
        });

        // All buttons are added to one map (needed to set "USE" status for items)
        shop.addPriceButtonToMap(currentTag, shipPriceButton);
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
