package com.nekobitlz.meteorite_attack.UI;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.views.activities.MainMenuActivity;
import com.nekobitlz.meteorite_attack.views.fragments.SettingsFragment;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.nekobitlz.meteorite_attack.views.fragments.MainMenuFragment.BACK_STACK;

public class SettingsFragmentTest {

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
                .replace(R.id.content, SettingsFragment.newInstance())
                .commit();
    }

    @Test
    public void checkBackgroundViews() {
        onView(ViewMatchers.withId(R.id.fragment_settings)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_settings)).check(matches(isDisplayed()));
    }

    @Test
    public void checkBackButton() {
        onView(withId(R.id.fragment_settings)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_main_menu)).check(doesNotExist());

        onView(withId(R.id.iv_back_btn)).perform(click());
        onView(withId(R.id.fragment_main_menu)).check(matches(isDisplayed()));
    }

    @Test
    public void checkSoundEnabled() {
        onView(withId(R.id.tv_sound_status)).check(matches(isDisplayed()));

        boolean isSoundEnabled = spm.getSoundStatus();
        Matcher<View> soundStatus = isSoundEnabled ? isChecked() : isNotChecked();

        onView(withId(R.id.cb_sound_status)).check(matches(soundStatus));
    }

    @Test
    public void checkVolume() {
        onView(withId(R.id.tv_effects_volume)).check(matches(isDisplayed()));
        onView(withId(R.id.sb_effects_volume)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_music_volume)).check(matches(isDisplayed()));
        onView(withId(R.id.sb_music_volume)).check(matches(isDisplayed()));
    }
}
