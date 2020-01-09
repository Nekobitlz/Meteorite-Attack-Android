package com.nekobitlz.meteorite_attack.UI;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import com.nekobitlz.meteorite_attack.R;
import com.nekobitlz.meteorite_attack.options.SharedPreferencesManager;
import com.nekobitlz.meteorite_attack.views.activities.MainMenuActivity;
import com.nekobitlz.meteorite_attack.views.fragments.ShopFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.nekobitlz.meteorite_attack.views.fragments.MainMenuFragment.BACK_STACK;
import static org.hamcrest.core.AllOf.allOf;

public class ShopFragmentTest {

    private Context appContext = InstrumentationRegistry.getTargetContext();
    private SharedPreferencesManager spm = new SharedPreferencesManager(appContext);
    private String tag = String.valueOf(R.drawable.spaceship_1_game);

    @Rule
    public ActivityTestRule<MainMenuActivity> activityRule = new ActivityTestRule<>(
            MainMenuActivity.class, true, true
    );

    @Before
    public void initFragment() {
        activityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(BACK_STACK)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.content, ShopFragment.newInstance())
                .commit();
    }

    @Test
    public void checkBackgroundViews() {
        onView(withId(R.id.fragment_shop)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_shop_title)).check(matches(isDisplayed()));
    }

    @Test
    public void checkShopFragmentViews() {
        onView(withId(R.id.vp_ship_pager)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.iv_ship), isDisplayed()))
                .check(matches(isEnabled()));
        onView(allOf(withId(R.id.iv_bullet), isDisplayed()))
                .check(matches(isEnabled()));
        onView(allOf(withId(R.id.iv_health), isDisplayed()))
                .check(matches(isEnabled()));
        onView(allOf(withId(R.id.iv_xscore), isDisplayed()))
                .check(matches(isEnabled()));
    }

    @Test
    public void checkBackButton() {
        onView(withId(R.id.fragment_shop)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_main_menu)).check(doesNotExist());

        onView(withId(R.id.iv_back_btn)).perform(click());
        onView(withId(R.id.fragment_main_menu)).check(matches(isDisplayed()));
     }

    @Test
    public void checkBuyButtons() {
        String shipStatus = spm.getStatus(tag);

        onView(allOf(withId(R.id.btn_ship_price), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.btn_ship_price), isDescendantOfA(nthChildOf(withId(R.id.vp_ship_pager)))))
                .check(matches(withText(shipStatus)));
    }

    @Test
    public void checkUpgradeStates() {
        String health = String.valueOf(spm.getHealth(tag));
        String weaponPower = String.valueOf(spm.getWeaponPower(tag));
        String xScore = String.valueOf(spm.getXScore(tag));

        //Checking *price* upgrade buttons
        onView(allOf(withId(R.id.btn_price_health), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.btn_price_bullet), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.btn_price_xscore), isDisplayed())).perform(click());

        //Checking levels state
        onView(allOf(withId(R.id.tv_health), isDescendantOfA(nthChildOf(withId(R.id.vp_ship_pager)))))
                .check(matches(withText(health)));
        onView(allOf(withId(R.id.tv_bullet), isDescendantOfA(nthChildOf(withId(R.id.vp_ship_pager)))))
                .check(matches(withText(weaponPower)));
        onView(allOf(withId(R.id.tv_xscore), isDescendantOfA(nthChildOf(withId(R.id.vp_ship_pager)))))
                .check(matches(withText(xScore)));
    }

    private static Matcher<View> nthChildOf(final Matcher<View> parentMatcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with "+ 0 +" child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(0).equals(view);
            }
        };
    }
}