package com.nekobitlz.meteorite_attack;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;

import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.views.ShopActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertTrue;

public class ShopActivityTest {

    private Context appContext = InstrumentationRegistry.getTargetContext();
    private SharedPreferencesManager spm = new SharedPreferencesManager(appContext);
    private String tag = String.valueOf(R.drawable.spaceship_1_game);

    @Rule
    public ActivityTestRule<ShopActivity> activityRule = new ActivityTestRule<>(
            ShopActivity.class, true, true
    );

    @Test
    public void checkBackgroundViews() {
        onView(withId(R.id.background)).check(matches(isDisplayed()));
        onView(withId(R.id.shop)).check(matches(isDisplayed()));
    }

    @Test
    public void checkShopFragmentViews() {
        onView(withId(R.id.pager)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.ship_image), isDisplayed()))
                .check(matches(isEnabled()));
        onView(allOf(withId(R.id.icon_bullet), isDisplayed()))
                .check(matches(isEnabled()));
        onView(allOf(withId(R.id.icon_health), isDisplayed()))
                .check(matches(isEnabled()));
        onView(allOf(withId(R.id.icon_xscore), isDisplayed()))
                .check(matches(isEnabled()));
    }

    @Test
    public void checkBackButton() {
        onView(withId(R.id.back)).perform(click());

        assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void checkBuyButtons() {
        String shipStatus = spm.getStatus(tag);

        onView(allOf(withId(R.id.ship_price), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.ship_price), isDescendantOfA(nthChildOf(withId(R.id.pager), 0))))
                .check(matches(withText(shipStatus)));
    }

    @Test
    public void checkUpgradeStates() {
        String health = String.valueOf(spm.getHealth(tag));
        String weaponPower = String.valueOf(spm.getWeaponPower(tag));
        String xScore = String.valueOf(spm.getXScore(tag));

        //Checking *price* upgrade buttons
        onView(allOf(withId(R.id.price_health), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.price_bullet), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.price_xscore), isDisplayed())).perform(click());

        //Checking levels state
        onView(allOf(withId(R.id.level_health), isDescendantOfA(nthChildOf(withId(R.id.pager), 0))))
                .check(matches(withText(health)));
        onView(allOf(withId(R.id.level_bullet), isDescendantOfA(nthChildOf(withId(R.id.pager), 0))))
                .check(matches(withText(weaponPower)));
        onView(allOf(withId(R.id.level_xscore), isDescendantOfA(nthChildOf(withId(R.id.pager), 0))))
                .check(matches(withText(xScore)));
    }

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with "+childPosition+" child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }
}