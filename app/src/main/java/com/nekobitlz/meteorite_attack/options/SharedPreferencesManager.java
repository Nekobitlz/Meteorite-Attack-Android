package com.nekobitlz.meteorite_attack.options;

import android.content.Context;
import android.content.SharedPreferences;
import com.nekobitlz.meteorite_attack.R;

/*
    Saving settings in memory
*/
public class SharedPreferencesManager {

    private String name = "MeteoriteAttack";
    private Context context;

    /*
        Settings initialization
    */
    public SharedPreferencesManager(Context context) {
        this.context = context;
    }

    /*
        Saves money in memory
    */
    public void saveMoney(int money) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.putInt("money", getMoney() + money);

        e.apply();
    }

    /*
        Saves high score and count of meteors and enemy that were destroyed
     */
    public void saveHighScore(int score, int meteorDestroyed, int enemyDestroyed) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.putInt("high_score", score);
        e.putInt("meteorite", meteorDestroyed);
        e.putInt("enemy", enemyDestroyed);

        e.apply();
    }

    /*
        Saves player image and weapon power
    */
    public void savePlayer(int playerImage, int weaponPower) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.putInt("player_image", playerImage);
        e.putInt("weapon_power", weaponPower);

        e.apply();
    }

    /*
        Saves shop status ("USED", "BUY", "USE") for shop item tag
    */
    public void saveStatus(String tag, String status) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.putString(tag, status);

        e.apply();
    }

    /*
        GETTERS
    */
    public int getHighScore() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("high_score", 0);
    }

    public int getMeteorDestroyed() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("meteorite", 0);
    }

    public int getEnemyDestroyed() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("enemy", 0);
    }

    public int getMoney() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("money", 0);
    }

    public int getPlayerImage() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("player_image", R.drawable.player_ship_1_red);
    }

    public int getWeaponPower() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("weapon_power", 1);
    }

    public String getStatus(String tag) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getString(tag, "NONE");
    }

    /*
        REMOVERS
        (need for tests)
    */
    public void removeMoney() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.remove("money");
        e.apply();
    }

    public void removeHighScore() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.remove("high score");
        e.remove("enemy");
        e.remove("meteorite");

        e.apply();
    }

    public void removePlayer() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.remove("player_image");
        e.remove("weapon_power");

        e.apply();
    }

    public void removeStatus(String tag) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.remove(tag);

        e.apply();
    }
}
