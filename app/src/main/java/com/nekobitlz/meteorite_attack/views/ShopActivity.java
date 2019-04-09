package com.nekobitlz.meteorite_attack.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.options.Shop;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

/*
    Activity where the shop is located
*/
public class ShopActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;

    private Shop shop;
    private ArrayList<Shop.ShopViewsSetup> shopViewsList;
    private ArrayList<ShopActivity.ShopItem> shopItemList;
    private SharedPreferencesManager spm;

    private String image;
    private String name;
    private String price;
    private String weaponPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        //Make the display full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Make the display always turn on if the activity is active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        shop = new Shop(this);
        spm = new SharedPreferencesManager(this);

        shopViewsList = new ArrayList<>();
        shopItemList = new ArrayList<>();

        XmlPullParser shopParser = getResources().getXml(R.xml.shop_list);
        parseXml(shopParser);

        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        setStatusTags();
        drawShopItem();
    }

    /*
        Sets status tags

            If there is saved data -> it gets it
            If not ->
                status of the first item is "USED",
                for the remaining items status is "BUY"
    */
    private void setStatusTags() {
        String firstItemTag = shopItemList.get(0).getName();

        if (android.text.TextUtils.equals(spm.getStatus(firstItemTag), "NONE")) {
            spm.saveStatus(firstItemTag, "USED");
        }

        for (ShopActivity.ShopItem item : shopItemList) {
            String itemName = item.getName();

            if (android.text.TextUtils.equals(spm.getStatus(itemName), "NONE")) {
                spm.saveStatus(itemName, "BUY");
            }
        }
    }

    /*
        Parsing XML file with a list of items in shop
    */
    private void parseXml(XmlPullParser parser) {
        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                    // Gets xml attributes
                    image = parser.getAttributeValue(0);
                    name = parser.getAttributeValue(1);
                    price = parser.getAttributeValue(2);
                    weaponPower = parser.getAttributeValue(3);

                    shopItemList.add(new ShopActivity.ShopItem(name, image, price, weaponPower));
                }

                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(this,
                    "Error loading shop items: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /*
        Draws all items in shop in the format:
              <LinearLayout> (rootContainer)
                <ScrollView>
                    <LinearLayout>
                      <LinearLayout> (Layout where each item is located)
                        <ImageView> (Item image)
                        <TextView> (Price)
                        <Button> ("USE", "USED", "BUY")
    */
    private void drawShopItem() {
        ScrollView scrollView = new ScrollView(this);
        LinearLayout linearLayout = new LinearLayout(this);

        LinearLayout.LayoutParams paramsMM = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams paramsMW = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams paramsWW = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT + 150, 60);

        paramsMW.setMargins(0, 30, 0, 0);
        paramsMW.gravity = Gravity.CENTER;

        paramsWW.setMargins(0, 30, 0, 30);
        paramsWW.gravity = Gravity.CENTER;

        priceParams.setMargins(0, 30, 0, 0);
        priceParams.gravity = Gravity.CENTER;

        scrollView.setLayoutParams(paramsMM);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(paramsMW);

        scrollView.addView(linearLayout);

        // Draws picture, price and processing button for each item
        for (final ShopActivity.ShopItem item : shopItemList) {
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setBackgroundColor(Color.WHITE);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setLayoutParams(paramsWW);

            // IMAGE
            ImageView playerImageView = new ImageView(this);

            playerImageView.setLayoutParams(paramsMW);
            playerImageView.setImageResource(item.getImage());

            itemLayout.addView(playerImageView);

            // PRICE
            TextView priceTextView = new TextView(this);

            priceTextView.setLayoutParams(priceParams);
            priceTextView.setTextColor(Color.WHITE);
            priceTextView.setGravity(Gravity.CENTER);
            priceTextView.setBackgroundColor(Color.parseColor("#A87D00"));

            // If item can be bought, the price is put
            if (spm.getStatus(item.getName()).equals("BUY")) {
                priceTextView.setText(item.getPrice());
                priceTextView.setTextSize(15);
            } else {
                priceTextView.setText("Purchased");
            }

            itemLayout.addView(priceTextView);

            // BUTTON
            Button statusButton = new Button(this);

            statusButton.setTextColor(Color.WHITE);
            statusButton.setText(spm.getStatus(item.getName()));
            statusButton.setBackgroundResource(R.drawable.custom_button_background);
            statusButton.setLayoutParams(paramsWW);

            itemLayout.addView(statusButton);

            // Sets onClickListener and initializes the processing of the item in the shop
            final Shop.ShopViewsSetup currentShopView = new Shop.ShopViewsSetup(
                    item.getName(), statusButton, priceTextView, Integer.parseInt(item.getPrice()),
                    new Pair<>(item.getImage(), item.getWeaponPower()));
            shopViewsList.add(currentShopView);

            View.OnClickListener oclStatusButton = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shop.processSelectedItem(shopViewsList, currentShopView);
                }
            };

            statusButton.setOnClickListener(oclStatusButton);
            linearLayout.addView(itemLayout);
        }

        LinearLayout rootContainer = findViewById(R.id.rootContainer);

        if (rootContainer != null) {
            rootContainer.addView(scrollView);
        }
    }

    /*
        Reads views clicks
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    /*
        Handles pressing "back" button
    */
    @Override
    public void onBackPressed() {
        finish();
    }

    /*
        Class describing characteristics of item in shop
    */
    public class ShopItem {
        private String name;
        private String image;
        private String price;
        private String weaponPower;

        public ShopItem(String name, String image, String price, String weaponPower) {
            this.name = name;
            this.image = image;
            this.price = price;
            this.weaponPower = weaponPower;
        }

        /*
            GETTERS
        */
        public String getName() {
            return name;
        }

        // Immediately gets a link to get a picture
        public int getImage() {
            return getResources().getIdentifier(image, "drawable", getPackageName());
        }

        public String getPrice() {
            return price;
        }

        public int getWeaponPower() {
            return Integer.parseInt(weaponPower);
        }
    }
}