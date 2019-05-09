package com.nekobitlz.meteorite_attack.views;

import android.os.Bundle;
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
import com.nekobitlz.meteorite_attack.views.ShopActivity.ShopItem;

import java.util.ArrayList;

/*
    Fragment that contains the item from the shop
*/
public class ShopItemFragment extends Fragment {
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    static final String SAVE_PAGE_NUMBER = "save_page_number";

    private ArrayList<ShopItem> shopItemList;
    private ShopItem shopItem;
    private int pageNumber;
    private int maxValue;

    private SharedPreferencesManager spm;
    private Shop shop;

    private String currentTag;
    private String health;
    private String weaponPower;
    private String xScore;

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
        shopItemList = ((ShopActivity) getActivity()).getShopItemList();
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shopItem = shopItemList.get(pageNumber);
        currentTag = String.valueOf(shopItem.getImage());
        maxValue = (pageNumber + 1) * 5;

        /*
            If the level of upgrade is higher than the maximum
                -> more performance can't be improved and "MAX" is displayed
        */
        health = String.valueOf(spm.getHealth(currentTag));
        health = Integer.parseInt(health) < maxValue ? health : "MAX";

        weaponPower = String.valueOf(spm.getWeaponPower(currentTag));
        weaponPower = Integer.parseInt(weaponPower) < maxValue ? weaponPower : "MAX";

        xScore = String.valueOf(spm.getXScore(currentTag));
        xScore = Integer.parseInt(xScore) < maxValue ? xScore : "MAX";

        // Setting all the characteristics on the view
        View view = inflater.inflate(R.layout.fragment_shop, null);

        ImageView shopImage = view.findViewById(R.id.ship_image);
        shopImage.setImageResource(shopItem.getImage());

        final TextView levelHealth = view.findViewById(R.id.level_health);
        levelHealth.setText(health);

        final TextView levelBullet = view.findViewById(R.id.level_bullet);
        levelBullet.setText(weaponPower);

        final TextView levelXScore = view.findViewById(R.id.level_xscore);
        levelXScore.setText(xScore);

        final Button priceHealth = view.findViewById(R.id.price_health);
        priceHealth.setText(health.equals("MAX") ? "" : shopItem.getUpgradePrice());
        priceHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop.processUpgrade(shopItem, "health", priceHealth, levelHealth, maxValue);
                ((ShopActivity) getActivity()).loadMoney();
            }
        });

        final Button priceBullet = view.findViewById(R.id.price_bullet);
        priceBullet.setText(weaponPower.equals("MAX") ? "" : shopItem.getUpgradePrice());
        priceBullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop.processUpgrade(shopItem, "weapon power", priceBullet, levelBullet, maxValue);
                ((ShopActivity) getActivity()).loadMoney();
            }
        });

        final Button priceXScore = view.findViewById(R.id.price_xscore);
        priceXScore.setText(xScore.equals("MAX") ? "" : shopItem.getUpgradePrice());
        priceXScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop.processUpgrade(shopItem, "score multiplier", priceXScore, levelXScore, maxValue);
                ((ShopActivity) getActivity()).loadMoney();
            }
        });

        String shopStatus = String.valueOf(shopItem.getImage());
        String shipPrice = spm.getStatus(shopStatus);

        final Button shipPriceButton = view.findViewById(R.id.ship_price);
        shipPriceButton.setText(shipPrice);
        shipPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop.processShip(shopItem, shipPriceButton);
                ((ShopActivity) getActivity()).loadMoney();
            }
        });

        // All buttons are added to one map (needed to set "USE" status for items)
        shop.addPriceButtonToMap(currentTag, shipPriceButton);

        return view;
    }

    /*
        Saves the last open item

        * NOT WORKING NOW  ¯\_(ツ)_/¯ *
    */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_PAGE_NUMBER, pageNumber);
    }
}
