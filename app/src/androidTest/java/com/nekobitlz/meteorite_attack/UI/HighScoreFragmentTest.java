package com.nekobitlz.meteorite_attack.UI;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.FragmentTransaction;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.views.activities.MainMenuActivity;
import com.nekobitlz.meteorite_attack.views.fragments.HighScoreFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.nekobitlz.meteorite_attack.views.fragments.MainMenuFragment.BACK_STACK;

public class HighScoreFragmentTest {

    private Context appContext = InstrumentationRegistry.getTargetContext();
    private SharedPreferencesManager spm = new SharedPreferencesManager(appContext);

    @Rule
    public ActivityTestRule<MainMenuActivity> activityRule = new ActivityTestRule<>(
            MainMenuActivity.class, true, true
    );

    @Before
    public void initFragment() {
        activityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(BACK_STACK)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.content, HighScoreFragment.newInstance())
                .commit();
    }

    @Test
    public void checkBackgroundViews() {
        onView(ViewMatchers.withId(R.id.fragment_high_score)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_high_score)).check(matches(isDisplayed()));
    }

    @Test
    public void checkBackButton() {
        onView(withId(R.id.fragment_high_score)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_main_menu)).check(doesNotExist());

        onView(withId(R.id.iv_back_btn)).perform(click());
        onView(withId(R.id.fragment_main_menu)).check(matches(isDisplayed()));
    }

    @Test
    public void checkHighScoreIsDisplayed() throws InterruptedException {
        Thread.sleep(100);
        onView(withId(R.id.fragment_high_score)).check(matches(isDisplayed()));

        String score = String.valueOf(spm.getHighScore());
        String meteorDestroyed = String.valueOf(spm.getMeteorDestroyed());
        String enemyDestroyed = String.valueOf(spm.getEnemyDestroyed());
        String borderDestroyed = String.valueOf(spm.getBorderDestroyed());
        String exploderDestroyed = String.valueOf(spm.getExploderDestroyed());

        onView(withId(R.id.tv_score_count)).check(matches(withText(score)));
        onView(withId(R.id.tv_meteor_count)).check(matches(withText(meteorDestroyed)));
        onView(withId(R.id.tv_enemy_ship_count)).check(matches(withText(enemyDestroyed)));
        onView(withId(R.id.tv_border_count)).check(matches(withText(borderDestroyed)));
        onView(withId(R.id.tv_exploder_count)).check(matches(withText(exploderDestroyed)));
    }
}
