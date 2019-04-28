package com.nekobitlz.meteorite_attack;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.views.HighScoreActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertTrue;

public class HighScoreActivityTest {

    private Context appContext = InstrumentationRegistry.getTargetContext();
    private SharedPreferencesManager spm = new SharedPreferencesManager(appContext);

    @Rule
    public ActivityTestRule<HighScoreActivity> activityRule = new ActivityTestRule<>(
            HighScoreActivity.class, true, true
    );

    @Test
    public void checkBackgroundViews() {
        onView(withId(R.id.background)).check(matches(isDisplayed()));
        onView(withId(R.id.high_score)).check(matches(isDisplayed()));
    }

    @Test
    public void checkBackButton() {
        onView(withId(R.id.back)).perform(click());

        assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void checkHighScoreIsDisplayed() throws InterruptedException {
        Thread.sleep(100);
        onView(withId(R.id.high_score_container)).check(matches(isDisplayed()));

        String score = String.valueOf(spm.getHighScore());
        String meteorDestroyed = String.valueOf(spm.getMeteorDestroyed());
        String enemyDestroyed = String.valueOf(spm.getEnemyDestroyed());
        String borderDestroyed = String.valueOf(spm.getBorderDestroyed());
        String exploderDestroyed = String.valueOf(spm.getExploderDestroyed());

        onView(withId(R.id.score_count)).check(matches(withText(score)));
        onView(withId(R.id.meteor_count)).check(matches(withText(meteorDestroyed)));
        onView(withId(R.id.enemy_count)).check(matches(withText(enemyDestroyed)));
        onView(withId(R.id.border_count)).check(matches(withText(borderDestroyed)));
        onView(withId(R.id.exploder_count)).check(matches(withText(exploderDestroyed)));
    }
}
