package com.nekobitlz.meteorite_attack.options;

import android.content.Context;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.nekobitlz.meteorite_attack.enums.ShopStatus;

import java.util.ArrayList;

/*
    Class shop implements
*/
public class Shop {
    private Context context;
    private ShopStatus status;
    private SharedPreferencesManager spm;

    private Button selectedButton;
    private int image, weaponPower;
    private TextView selectedPrice;
    private int currentMoney, moneyPrice;

    public Shop(Context context) {
        this.context = context;
        this.spm = new SharedPreferencesManager(context);
    }

    /*
        Handles click on item

        If status of item is:
                Use -> Ship changing
                Used -> Nothing happens and toast pops up that the ship is already in use
                Buy -> If user has money, money is spent and ship goes to status "Used",
                       if not, then a toast with error comes out
    */
    public void processSelectedItem(ArrayList<ShopViewsSetup> shopViewsList, ShopViewsSetup shopViewsSetup) {
        selectedButton = shopViewsSetup.getButton();
        selectedPrice = shopViewsSetup.getPriceView();
        status = shopViewsSetup.getStatus();
        moneyPrice = shopViewsSetup.getPrice();

        image = shopViewsSetup.getPlayerSetup().first;
        weaponPower = shopViewsSetup.getPlayerSetup().second;

        switch (status) {
            case Use: {
                makeStatusUsed(shopViewsList);
                Toast.makeText(context, "Ship changed", Toast.LENGTH_SHORT).show();
            }
            break;

            case Used: {
                Toast.makeText(context, "You already use this ship", Toast.LENGTH_SHORT).show();
            }
            break;

            case Buy: {
                currentMoney = spm.getMoney();

                if (currentMoney >= moneyPrice) {
                    spm.saveMoney(-moneyPrice);

                    selectedPrice.setText("");
                    makeStatusUsed(shopViewsList);

                    Toast.makeText(context, "You successfully buy new ship!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "You don't have enough money", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }

        shopViewsSetup.setStatus(status);
    }

    /*
        Saves player status and sets item status to "Used"
        Also sets an already used item to "Use" status
    */
    private void makeStatusUsed(ArrayList<ShopViewsSetup> shopViewsList) {
        status = ShopStatus.Used;
        spm.savePlayer(image, weaponPower);
        selectedButton.setText("USED");

        for (ShopViewsSetup aShopViewsList : shopViewsList) {
            ShopStatus itemStatus = aShopViewsList.getStatus();

            if (itemStatus == ShopStatus.Used) {
                aShopViewsList.getButton().setText("USE");
                aShopViewsList.setStatus(ShopStatus.Use);
            }
        }
    }

    /*
        Auxiliary class for storing shop item information
    */
    public static class ShopViewsSetup {
        private ShopStatus status;
        private Button button;
        private TextView priceView;
        private int price;
        private Pair<Integer, Integer> playerSetup;

        public ShopViewsSetup(
                ShopStatus status, Button button, TextView priceView,
                int price, Pair<Integer, Integer> playerSetup) {
            this.status = status;
            this.button = button;
            this.priceView = priceView;
            this.price = price;
            this.playerSetup = playerSetup;
        }

        /*
            GETTERS & SETTERS
        */
        public void setStatus(ShopStatus status) {
            this.status = status;
        }

        public ShopStatus getStatus() {
            return status;
        }

        public Button getButton() {
            return button;
        }

        public TextView getPriceView() {
            return priceView;
        }

        public int getPrice() {
            return price;
        }

        public Pair<Integer, Integer> getPlayerSetup() {
            return playerSetup;
        }
    }
}
