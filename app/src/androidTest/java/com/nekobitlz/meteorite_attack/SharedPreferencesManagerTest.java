package com.nekobitlz.meteorite_attack;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SharedPreferencesManagerTest {
    private Context appContext = InstrumentationRegistry.getContext();
    private SharedPreferencesManager spm = new SharedPreferencesManager(appContext);
    private String tag = String.valueOf(R.drawable.player_ship_1_red);

    @After
    public void removePreferences() {
        spm.removeMoney();
        spm.removeHighScore();
        spm.removePlayer();
        spm.removeStatus(tag);
    }

    @Test
    public void saveMoney() {
        //check default
        assertEquals(0, spm.getMoney());

        int money = 100;
        spm.saveMoney(money);

        assertEquals(money, spm.getMoney());
    }

    @Test
    public void saveHighScore() {
        //check default
        assertEquals(0, spm.getHighScore());

        int highScore = 1000;
        int enemyDestroyed = 3;
        int meteorDestroyed = 2;

        spm.saveHighScore(highScore, meteorDestroyed, enemyDestroyed);

        assertEquals(highScore, spm.getHighScore());
        assertEquals(enemyDestroyed, spm.getEnemyDestroyed());
        assertEquals(meteorDestroyed, spm.getMeteorDestroyed());
    }

    @Test
    public void savePlayer() {
        //check default
        assertEquals(R.drawable.player_ship_1_red, spm.getPlayerImage());
        assertEquals(1, spm.getWeaponPower());

        int image = R.drawable.player_ship_2_red;
        int weaponPower = 5;
        spm.savePlayer(image, weaponPower);

        assertEquals(image, spm.getPlayerImage());
        assertEquals(weaponPower, spm.getWeaponPower());
    }

    @Test
    public void saveStatus() {
        //check default
        assertEquals("NONE", spm.getStatus(tag));

        String status = "USED";
        spm.saveStatus(tag, status);

        assertEquals(status, spm.getStatus(tag));
    }
}
