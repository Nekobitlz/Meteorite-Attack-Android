package com.nekobitlz.meteorite_attack.Instrumented;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SharedPreferencesManagerTest {
    private Context appContext = InstrumentationRegistry.getContext();
    private SharedPreferencesManager spm = new SharedPreferencesManager(appContext);
    private String tag = String.valueOf(R.drawable.spaceship_1_game);

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
        assertEquals(0, spm.getMeteorDestroyed());
        assertEquals(0, spm.getEnemyDestroyed());
        assertEquals(0, spm.getBorderDestroyed());
        assertEquals(0, spm.getExploderDestroyed());

        int highScore = 1000;
        int enemyDestroyed = 3;
        int meteorDestroyed = 2;
        int borderDestroyed = 5;
        int exploderDestroyed = 1;

        spm.saveHighScore(
                highScore, meteorDestroyed, enemyDestroyed, borderDestroyed, exploderDestroyed
        );

        assertEquals(highScore, spm.getHighScore());
        assertEquals(enemyDestroyed, spm.getEnemyDestroyed());
        assertEquals(meteorDestroyed, spm.getMeteorDestroyed());
        assertEquals(borderDestroyed, spm.getBorderDestroyed());
        assertEquals(exploderDestroyed, spm.getExploderDestroyed());
    }

    @Test
    public void savePlayer() {
        //check default
        assertEquals(R.drawable.spaceship_1_game, spm.getPlayerImage());
        assertEquals(R.drawable.laser_blue_1, spm.getLaserImage());
        assertEquals(1, spm.getLevel());

        int playerImage = R.drawable.spaceship_5_game;
        int laserImage = R.drawable.laser_blue_5;
        int level = 5;

        spm.savePlayer(playerImage, laserImage, level);

        assertEquals(playerImage, spm.getPlayerImage());
        assertEquals(laserImage, spm.getLaserImage());
        assertEquals(level, spm.getLevel());
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

        spm.saveUpgrade(tag, health, xScore, weaponPower);

        assertEquals(health, spm.getHealth(tag));
        assertEquals(weaponPower, spm.getWeaponPower(tag));
        assertEquals(xScore, spm.getXScore(tag));
    }

    @Test
    public void saveSound() {
        //check default
        assertTrue(spm.getSoundStatus());
        assertEquals(50, spm.getEffectsVolume());
        assertEquals(50, spm.getMusicVolume());

        int effectsVolume = 100;
        int musicVolume = 100;

        spm.saveSound(false, effectsVolume, musicVolume);

        assertFalse(spm.getSoundStatus());
        assertEquals(effectsVolume, spm.getEffectsVolume());
        assertEquals(musicVolume, spm.getMusicVolume());
    }
}