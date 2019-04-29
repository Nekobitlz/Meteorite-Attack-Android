package com.nekobitlz.meteorite_attack.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private TextView money;

    private ArrayList<ShopActivity.ShopItem> shopItemList;
    private SharedPreferencesManager spm;

    private String image;
    private String health;
    private String shipPrice;
    private String xScore;
    private String upgradePrice;
    private String weaponPower;
    private String laser;
    private String level;

    private int PAGE_COUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        //Make the display full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Make the display always turn on if the activity is active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        spm = new SharedPreferencesManager(this);
        shopItemList = new ArrayList<>();

        XmlPullParser shopParser = getResources().getXml(R.xml.shop_list);
        parseXml(shopParser);
        setStatusTags();

        PAGE_COUNT = shopItemList.size();

        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        money = findViewById(R.id.money);
        loadMoney();

        pager = findViewById(R.id.pager);
        pagerAdapter = new ShopPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }

    /*
        Sets status tags

            STATUS == BUTTON TEXT

            If there is saved data -> it gets it
            If not ->
                status of the first item is "USED",
                for the remaining items status is equal to price
    */
    private void setStatusTags() {
        String firstItemTag = String.valueOf(shopItemList.get(0).getImage());

        if (android.text.TextUtils.equals(spm.getStatus(firstItemTag), "NONE")) {
            spm.saveStatus(firstItemTag, "USED");
            spm.savePlayer(shopItemList.get(0).getImage(),
                            shopItemList.get(0).getLaser(),
                                 shopItemList.get(0).getLevel()
            );
        }

        for (ShopActivity.ShopItem item : shopItemList) {
            String itemName = String.valueOf(item.getImage());

            if (android.text.TextUtils.equals(spm.getStatus(itemName), "NONE")) {
                spm.saveStatus(itemName, item.getShipPrice());
                spm.saveUpgrade(
                        itemName,
                        Integer.parseInt(item.getHealth()),
                        Integer.parseInt(item.getXScore()),
                        Integer.parseInt(item.getWeaponPower()));
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
                    health = parser.getAttributeValue(0);
                    image = parser.getAttributeValue(1);
                    laser = parser.getAttributeValue(2);
                    level = parser.getAttributeValue(3);
                    shipPrice = parser.getAttributeValue(4);
                    upgradePrice = parser.getAttributeValue(5);
                    weaponPower = parser.getAttributeValue(6);
                    xScore = parser.getAttributeValue(7);

                    shopItemList.add(
                            new ShopActivity.ShopItem(
                                    image, shipPrice, laser, level, upgradePrice, health, weaponPower, xScore)
                    );
                }

                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(this,
                    "Error loading shop items: " + t.toString(), Toast.LENGTH_LONG).show();
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
        Loads your saved money
    */
    @SuppressLint("SetTextI18n")
    public void loadMoney() {
        money.setText(spm.getMoney() + "");
    }

    /*
        Returns a list of all items in the shop
    */
    public ArrayList<ShopItem> getShopItemList() {
        return shopItemList;
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
        private String image;
        private String shipPrice;
        private String upgradePrice;
        private String health;
        private String weaponPower;
        private String xScore;
        private String level;
        private String laser;

        public ShopItem(String image, String shipPrice, String laser, String level,
                        String upgradePrice, String health, String weaponPower, String xScore) {
            this.image = image;
            this.shipPrice = shipPrice;
            this.laser = laser;
            this.level = level;
            this.upgradePrice = upgradePrice;
            this.health = health;
            this.weaponPower = weaponPower;
            this.xScore = xScore;
        }

        /*
            GETTERS
        */

        // Immediately gets a link to get a picture
        public int getImage() {
            return getResources().getIdentifier(image, "drawable", getPackageName());
        }

        public String getShipPrice() {
            return shipPrice;
        }

        public String getUpgradePrice() {
            return upgradePrice;
        }

        public String getHealth() {
            return health;
        }

        public String getXScore() {
            return xScore;
        }

        public String getWeaponPower() {
            return weaponPower;
        }

        public int getLevel() {
            return Integer.parseInt(level);
        }

        public int getLaser() {
            return getResources().getIdentifier(laser, "drawable", getPackageName());
        }
    }

    /*
        Adapter for the fragment that contains the item from the shop
    */
    private class ShopPagerAdapter extends FragmentPagerAdapter {

        public ShopPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /*
            GETTERS
        */
        @Override
        public Fragment getItem(int position) {
            return ShopItemFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }
}