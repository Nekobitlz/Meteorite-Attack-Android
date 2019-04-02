package com.nekobitlz.meteorite_attack.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.enums.ShopStatus;
import com.nekobitlz.meteorite_attack.options.Shop;

import java.util.ArrayList;

import static com.nekobitlz.meteorite_attack.views.MainMenuActivity.loadMoney;

/*
    Activity where the shop is located
*/
public class ShopActivity extends AppCompatActivity implements View.OnClickListener {
    private final int PRICE_1 = 0;
    private final int PRICE_2 = 100;
    private final int PRICE_3 = 300;

    private ImageView back;
    private TextView pricePlayer1, pricePlayer2, pricePlayer3;
    private Button buttonPlayer1, buttonPlayer2, buttonPlayer3;

    private Shop shop;
    private ArrayList<Shop.ShopViewsSetup> shopViewsList;
    private ArrayList<Pair<Integer, Integer>> playerSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        //Make the display full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Make the display always turn on if the activity is active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        shop = new Shop(this);

        shopViewsList = new ArrayList<>();
        playerSetup = new ArrayList<>();

        back = findViewById(R.id.back);

        pricePlayer1 = findViewById(R.id.pricePlayer1);
        pricePlayer2 = findViewById(R.id.pricePlayer2);
        pricePlayer3 = findViewById(R.id.pricePlayer3);

        buttonPlayer1 = findViewById(R.id.buttonPlayer1);
        buttonPlayer2 = findViewById(R.id.buttonPlayer2);
        buttonPlayer3 = findViewById(R.id.buttonPlayer3);

        back.setOnClickListener(this);
        buttonPlayer1.setOnClickListener(this);
        buttonPlayer2.setOnClickListener(this);
        buttonPlayer3.setOnClickListener(this);

        initializePlayerSettings();
        initializeShopViewsList();
    }

    /*
        Adds all Views in one list
    */
    private void initializeShopViewsList() {
        shopViewsList.add(new Shop.ShopViewsSetup(
                ShopStatus.Used, buttonPlayer1, pricePlayer1, PRICE_1, playerSetup.get(0)));
        shopViewsList.add(new Shop.ShopViewsSetup(
                ShopStatus.Buy, buttonPlayer2, pricePlayer2, PRICE_2, playerSetup.get(1)));
        shopViewsList.add(new Shop.ShopViewsSetup(
                ShopStatus.Buy, buttonPlayer3, pricePlayer3, PRICE_3, playerSetup.get(2)));
    }

    /*
        Adds all player settings in one list
    */
    private void initializePlayerSettings() {
        playerSetup.add(0, new Pair<>(R.drawable.player_ship_1_red, 1));
        playerSetup.add(1, new Pair<>(R.drawable.player_ship_2_red, 3));
        playerSetup.add(2, new Pair<>(R.drawable.player_ship_3_red, 5));
    }

    /*
        Reads views clicks

        If user press the button, we call state processing of this item
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlayer1: {
                shop.processSelectedItem(shopViewsList, shopViewsList.get(0));
            }
            break;

            case R.id.buttonPlayer2: {
                shop.processSelectedItem(shopViewsList, shopViewsList.get(1));
            }
            break;

            case R.id.buttonPlayer3: {
                shop.processSelectedItem(shopViewsList, shopViewsList.get(2));
            }
            break;

            case R.id.back:
                finish();
                loadMoney();
                break;
        }
    }

    /*
        Handles pressing "back" button

        It is need to update the money in the Main Menu
    */
    @Override
    public void onBackPressed() {
        finish();
        loadMoney();
    }
}
