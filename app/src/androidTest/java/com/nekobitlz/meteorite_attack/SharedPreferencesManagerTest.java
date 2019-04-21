package com.nekobitlz.meteorite_attack;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class SharedPreferencesManagerTest {
    private Context appContext = InstrumentationRegistry.getContext();
    private SharedPreferencesManager spm = new SharedPreferencesManager(appContext);
    private String tag = String.valueOf(R.drawable.spaceship_1_game);

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
        assertEquals(R.drawable.spaceship_1_game, spm.getPlayerImage());

        int image = R.drawable.spaceship_2_game;
        spm.savePlayer(image);

        assertEquals(image, spm.getPlayerImage());
    }

    @Test
    public void saveStatus() {
        //check default
        assertEquals("NONE", spm.getStatus(tag));

        String status = "USED";
        spm.saveStatus(tag, status);

        assertEquals(status, spm.getStatus(tag));
    }

    @Test
    public void saveUpgrade() {
        //check default
        assertEquals(1, spm.getWeaponPower(tag));
        assertEquals(1, spm.getHealth(tag));
        assertEquals(1, spm.getXScore(tag));

        int health = 4;
        int weaponPower = 3;
        int xScore = 5;

        spm.saveUpgrade(tag, health, weaponPower, xScore);

        assertEquals(health, spm.getHealth(tag));
        assertEquals(weaponPower, spm.getWeaponPower(tag));
        assertEquals(xScore, spm.getXScore(tag));
    }

    @Test
    public void saveSound() {
        //check default
        assertTrue(spm.getSoundStatus());
        assertEquals(1, spm.getVolume());

        spm.saveSound(false, 50);

        assertFalse(spm.getSoundStatus());
        assertEquals(50, spm.getVolume());
    }
}
