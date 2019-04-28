package com.nekobitlz.meteorite_attack.UI;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.views.SettingsActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertTrue;

public class SettingsActivityTest {

    private Context appContext = InstrumentationRegistry.getTargetContext();
    private SharedPreferencesManager spm = new SharedPreferencesManager(appContext);

    @Rule
    public ActivityTestRule<SettingsActivity> activityRule = new ActivityTestRule<>(
            SettingsActivity.class, true, true
    );

    @Test
    public void checkBackgroundViews() {
        onView(ViewMatchers.withId(R.id.background)).check(matches(isDisplayed()));
        onView(withId(R.id.settings)).check(matches(isDisplayed()));
    }

    @Test
    public void checkBackButton() {
        onView(withId(R.id.back)).perform(click());

        assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void checkSoundEnabled() {
        onView(withId(R.id.sound_status)).check(matches(isDisplayed()));

        boolean isSoundEnabled = spm.getSoundStatus();
        Matcher<View> soundStatus = isSoundEnabled ? isChecked() : isNotChecked();

        onView(withId(R.id.sound_checkbox)).check(matches(soundStatus));
    }

    @Test
    public void checkVolume() {
        onView(withId(R.id.effects_volume)).check(matches(isDisplayed()));
        onView(withId(R.id.effects_seekbar)).check(matches(isDisplayed()));
        onView(withId(R.id.music_volume)).check(matches(isDisplayed()));
        onView(withId(R.id.music_seekbar)).check(matches(isDisplayed()));
    }
}
