package com.nekobitlz.meteorite_attack;

import android.content.Context;
import android.content.SharedPreferences;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import android.support.test.InstrumentationRegistry;

import com.nekobitlz.meteorite_attack.views.GameView;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SharedPreferencesManagerTest {
    private Context appContext = InstrumentationRegistry.getTargetContext();
    private SharedPreferencesManager spm = new SharedPreferencesManager(appContext);

    @After
    public void removeSaved() {
        spm.removeHighScore();
        spm.removeMoney();
    }

    @Test
    public void saveMoney() throws Exception {
        int money = 100;
        spm.saveMoney(money);

        assertEquals(money, spm.getMoney());
    }

    @Test
    public void saveHighScore() throws Exception {
        spm.getHighScore();
        assertEquals(0, spm.getHighScore());

        int highScore = 1000;
        int enemyDestroyed = 3;
        int meteorDestroyed = 2;

        spm.saveHighScore(highScore, meteorDestroyed, enemyDestroyed);

        assertEquals(highScore, spm.getHighScore());
        assertEquals(enemyDestroyed, spm.getEnemyDestroyed());
        assertEquals(meteorDestroyed, spm.getMeteorDestroyed());
    }
}
