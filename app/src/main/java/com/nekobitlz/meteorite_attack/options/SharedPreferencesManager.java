package com.nekobitlz.meteorite_attack.options;

import android.content.Context;
import android.content.SharedPreferences;

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

        e.commit();
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

        e.commit();
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

    /*
        REMOVERS
        (need for tests)
    */
    public void removeMoney() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.remove("money");
        e.commit();
    }

    public void removeHighScore() {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.remove("high score");
        e.remove("enemy");
        e.remove("meteorite");

        e.commit();
    }
}
