package com.nekobitlz.meteorite_attack.options;

import android.content.Context;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
    Class shop implements
*/
public class Shop {
    private Context context;
    private String status;
    private String statusTag;
    private SharedPreferencesManager spm;

    private Button selectedButton;
    private TextView selectedPrice;

    private int image;
    private int weaponPower;
    private int currentMoney;
    private int moneyPrice;

    private String use = "USE";
    private String used = "USED";

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
        statusTag = shopViewsSetup.getStatusTag();
        status = spm.getStatus(statusTag);
        moneyPrice = shopViewsSetup.getPrice();

        image = shopViewsSetup.getPlayerSetup().first;
        weaponPower = shopViewsSetup.getPlayerSetup().second;

        switch (status) {
            case "USE" : {
                makeStatusUsed(shopViewsList);
                Toast.makeText(context, "Ship changed", Toast.LENGTH_SHORT).show();
            }
            break;

            case "USED" : {
                Toast.makeText(context, "You already use this ship", Toast.LENGTH_SHORT).show();
            }
            break;

            case "BUY" : {
                currentMoney = spm.getMoney();

                if (currentMoney >= moneyPrice) {
                    spm.saveMoney(-moneyPrice);

                    selectedPrice.setTextSize(14);
                    selectedPrice.setText("Purchased");
                    makeStatusUsed(shopViewsList);

                    Toast.makeText(context, "You successfully buy new ship!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "You don't have enough money", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }

        spm.saveStatus(statusTag, status);
    }

    /*
        Saves player status and sets item status to "Used"
        Also sets an already used item to "Use" status
    */
    private void makeStatusUsed(ArrayList<ShopViewsSetup> shopViewsList) {
        status = used;

        //default setText() method not working
        selectedButton.post(new Runnable() {
            @Override
            public void run() {
                selectedButton.setText(status);
            }
        });

        //Saving parameters for further load
        spm.savePlayer(image, weaponPower);
        spm.saveStatus(String.valueOf(image), status);

        //Remove status "used" from the previous used ship
        for (ShopViewsSetup item : shopViewsList) {
            String itemStatus = spm.getStatus(item.getStatusTag());

            if (itemStatus.equals(used)) {
                item.getButton().setText(use);
                spm.saveStatus(item.getStatusTag(), use);
                spm.saveStatus(String.valueOf(item.playerSetup.first), use);
            }
        }
    }

    /*
        Auxiliary class for storing shop item information with Views
    */
    public static class ShopViewsSetup {
        private String statusTag;
        private Button button;
        private TextView priceView;
        private int price;
        private Pair<Integer, Integer> playerSetup;

        public ShopViewsSetup(
                String statusTag, Button button, TextView priceView,
                int price, Pair<Integer, Integer> playerSetup) {
            this.statusTag = statusTag;
            this.button = button;
            this.priceView = priceView;
            this.price = price;
            this.playerSetup = playerSetup;
        }

        /*
            GETTERS
        */
        public String getStatusTag() {
            return statusTag;
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
