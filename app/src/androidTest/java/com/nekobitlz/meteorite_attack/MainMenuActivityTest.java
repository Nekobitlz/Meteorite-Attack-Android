package com.nekobitlz.meteorite_attack;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.views.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainMenuActivityTest {

    private Context appContext = InstrumentationRegistry.getTargetContext();
    private SharedPreferencesManager spm = new SharedPreferencesManager(appContext);

    @Rule
    public ActivityTestRule<MainMenuActivity> activityRule = new ActivityTestRule<>(
            MainMenuActivity.class, true, true
    );

    @Before
    public void initComponents() {
        Intents.init();
    }

    @Test
    public void checkBackgroundViews() {
        onView(withId(R.id.game_logo)).check(matches(isDisplayed()));
        onView(withId(R.id.background)).check(matches(isDisplayed()));
    }

    @Test
    public void checkMoney() {
        onView(withId(R.id.icon_coin)).check(matches(isDisplayed()));

        String money = String.valueOf(spm.getMoney());

        onView(withId(R.id.money)).check(matches(withText(money)));
    }

    @Test
    public void checkExitButton() {
       onView(ViewMatchers.withId(R.id.exit)).perform(click());

       assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void checkPlayButton() {
       onView(withId(R.id.play)).perform(click());
       intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void checkSettingsButton() {
        onView(withId(R.id.settings)).perform(click());
        intended(hasComponent(SettingsActivity.class.getName()));
    }

    @Test
    public void checkShopButton() {
        onView(withId(R.id.shop)).perform(click());
        intended(hasComponent(ShopActivity.class.getName()));
    }

    @Test
    public void checkHighScoreButton() {
        onView(withId(R.id.high_score)).perform(click());
        intended(hasComponent(HighScoreActivity.class.getName()));
    }
}
