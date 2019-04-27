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
    public void saveHighScore(int score, int meteorDestroyed, int enemyDestroyed,
                              int borderDestroyed, int exploderDestroyed) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.putInt("high_score", score);
        e.putInt("meteorite", meteorDestroyed);
        e.putInt("enemy", enemyDestroyed);
        e.putInt("border", borderDestroyed);
        e.putInt("exploder", exploderDestroyed);

        e.apply();
    }

    /*
        Saves player image, laser image and difficulty level
    */
    public void savePlayer(int playerImage, int laserImage, int level) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.putInt("player_image", playerImage);
        e.putInt("laser_image", laserImage);
        e.putInt("level", level);

        e.apply();
    }

    /*
        Saves levels of improved health, score and damage.
    */
    public void saveUpgrade(String tag, int health, int xScore, int weaponPower) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.putInt(tag + "_health", health);
        e.putInt(tag + "_x_score", xScore);
        e.putInt(tag + "_weapon_power", weaponPower);

        e.apply();
    }

    /*
        Saves shop status ("USED", "*PRICE*", "USE") for shop item tag
    */
    public void saveStatus(String tag, String status) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.putString(tag, status);

        e.apply();
    }

    /*
        Saves sound state
    */
    public void saveSound(boolean isEnabled, int effectsVolume, int musicVolume) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.putInt("effects_volume", effectsVolume);
        e.putBoolean("sound_status", isEnabled);
        e.putInt("music_volume", musicVolume);

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

    public int getBorderDestroyed() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("border", 0);
    }

    public int getExploderDestroyed() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("exploder", 0);
    }

    public int getMoney() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("money", 0);
    }

    public int getPlayerImage() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("player_image", R.drawable.spaceship_1_game);
    }

    public int getLaserImage() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("laser_image", R.drawable.laser_blue_1);
    }

    public int getLevel() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("level", 1);
    }

    public int getHealth(String tag) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt(tag + "_health", 1);
    }

    public int getWeaponPower(String tag) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt(tag + "_weapon_power", 1);
    }

    public int getXScore(String tag) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt(tag + "_x_score", 1);
    }

    public String getStatus(String tag) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getString(tag, "NONE");
    }

    public boolean getSoundStatus() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getBoolean("sound_status", true);
    }

    public int getEffectsVolume() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("effects_volume", 50);
    }

    public int getMusicVolume() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sp.getInt("music_volume", 50);
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
