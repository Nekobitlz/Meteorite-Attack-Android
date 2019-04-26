package com.nekobitlz.meteorite_attack.options;

import android.content.Context;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.nekobitlz.meteorite_attack.views.ShopActivity;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.PendingIntent.getActivity;

/*
    Class shop implements
*/
public class Shop {
    private Context context;
    private String statusTag;
    private String upgradeTag;
    private SharedPreferencesManager spm;
    private static HashMap<String, Button> shipPriceButtonMap = new HashMap<>();

    private int weaponPower;
    private int currentMoney;
    private int moneyPrice;
    private int level;
    private int xScore;
    private int health;
    private int shipPrice;

    private String use = "USE";
    private String used = "USED";

    public Shop(Context context) {
        this.context = context;
        this.spm = new SharedPreferencesManager(context);
    }

    /*
        Handles click on upgrade

        If upgrade name is
            "health" ->
                    If the level of upgrade is not more than 5 & the user has enough money
                                -> health increases

            "score multiplier" (xScore) ->
                    If the level of upgrade is not more than 5 & the user has enough money
                                -> score multiplier increases

            "weapon power" ->
                    If the level of upgrade is not more than 5 & the user has enough money
                                -> weapon power increases
    */
    public void processUpgrade(ShopActivity.ShopItem shopItem, String upgradeName,
                               Button priceButton, TextView levelView, int maxValue) {
        moneyPrice = Integer.parseInt(shopItem.getUpgradePrice());
        upgradeTag = String.valueOf(shopItem.getImage());
        health = spm.getHealth(upgradeTag);
        xScore = spm.getXScore(upgradeTag);
        weaponPower = spm.getWeaponPower(upgradeTag);
        currentMoney = spm.getMoney();

        switch (upgradeName) {
            case "health" : level = spm.getHealth(upgradeTag);
            break;
            case "score multiplier" : level = spm.getXScore(upgradeTag);
            break;
            case "weapon power" : level = spm.getWeaponPower(upgradeTag);
            break;
            default: level = 1; /* NEVER HAPPEN */
        }

        if (level < maxValue) {
            if (moneyPrice <= currentMoney) {
                level++;

                switch (upgradeName) {
                    case "health" : health++;
                    break;
                    case "score multiplier" : xScore++;
                    break;
                    case "weapon power" : weaponPower++;
                    break;
                }

                spm.saveMoney(-moneyPrice);
                spm.saveUpgrade(upgradeTag, health, xScore, weaponPower);

                if (level < maxValue) {
                    levelView.setText(String.valueOf(level));
                } else {
                    levelView.setText("MAX");
                    priceButton.setText("");
                    priceButton.setActivated(false);
                }

                Toast.makeText(context, "You successfully upgrade " + upgradeName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "You don't have enough money", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "You have already reached maximum level", Toast.LENGTH_SHORT).show();
        }
    }

    /*
        Handles click on price button

        If status of item is:
                Use -> Ship changing
                Used -> Nothing happens and toast pops up that the ship is already in use
                *Price* -> If user has money, money is spent and ship goes to status "Used",
                       if not, then a toast with error comes out
    */
    public void processShip(ShopActivity.ShopItem shopItem, Button shipPriceButton) {
        shipPrice = Integer.parseInt(shopItem.getShipPrice());
        statusTag = String.valueOf(shopItem.getImage());
        currentMoney = spm.getMoney();

        switch (shipPriceButton.getText().toString()) {
            case "USE" : {
                makeStatusUsed(shopItem, shipPriceButton);

                Toast.makeText(context, "Ship changed", Toast.LENGTH_SHORT).show();
            }
            break;
            case "USED" : {
                Toast.makeText(context, "You already use this ship", Toast.LENGTH_SHORT).show();
            }
            break;
            /* default situation will be when the button displays PRICE */
            default: {
                if (shipPrice <= currentMoney) {
                    spm.saveMoney(-shipPrice);
                    makeStatusUsed(shopItem, shipPriceButton);

                    Toast.makeText(context, "You successfully buy new ship!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "You don't have enough money", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    /*
        Saves player status and sets item status to "Used"
        Also sets an already used item to "Use" status
    */
    private void makeStatusUsed(ShopActivity.ShopItem shopItem, Button shipPriceButton) {
        for (HashMap.Entry<String, Button> entry: shipPriceButtonMap.entrySet()) {
            String tag = entry.getKey();
            final Button button = entry.getValue();

            if (button.getText().toString().equals(used)) {
                spm.saveStatus(tag, use);
                button.setText(use);
            }
        }

        spm.savePlayer(shopItem.getImage(), shopItem.getLaser(), shopItem.getLevel());
        spm.saveStatus(statusTag, used);
        shipPriceButton.setText(used);
    }

    /*
        Adds a button to the general list of buttons (needed for setting "USE" status)
    */
    public void addPriceButtonToMap(String statusTag, Button shipPriceButton) {
        shipPriceButtonMap.put(statusTag, shipPriceButton);
    }
}
