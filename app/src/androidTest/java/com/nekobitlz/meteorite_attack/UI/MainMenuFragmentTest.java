package com.nekobitlz.meteorite_attack.UI;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.options.Utils;
import com.nekobitlz.meteorite_attack.views.activities.MainActivity;
import com.nekobitlz.meteorite_attack.views.activities.MainMenuActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainMenuFragmentTest {

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

    @After
    public void releaseComponents() {
        Intents.release();
    }

    @Test
    public void checkBackgroundViews() {
        onView(withId(R.id.fragment_main_menu)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.iv_game_logo)).check(matches(isDisplayed()));
    }

    @Test
    public void checkMoney() {
        String money = Utils.formatMoney(String.valueOf(spm.getMoney()));

        onView(withId(R.id.tv_money)).check(matches(withText(money)));
    }

    @Test
    public void checkExitButton() {
       onView(ViewMatchers.withId(R.id.btn_exit)).perform(click());

       assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void checkPlayButton() {
       onView(withId(R.id.btn_play)).perform(click());
       intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void checkSettingsButton() {
        onView(withId(R.id.ib_settings)).perform(click());
        onView(withId(R.id.fragment_settings)).check(matches(isDisplayed()));
    }

    @Test
    public void checkShopButton() {
        onView(withId(R.id.ib_shop)).perform(click());
        onView(withId(R.id.fragment_shop)).check(matches(isDisplayed()));
    }

    @Test
    public void checkHighScoreButton() {
        onView(withId(R.id.ib_high_score)).perform(click());
        onView(withId(R.id.fragment_high_score)).check(matches(isDisplayed()));
    }
}
