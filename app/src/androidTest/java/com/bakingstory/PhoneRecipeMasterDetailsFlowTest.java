package com.bakingstory;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.bakingstory.recipes_collection.ActivityRecipesList;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by emil.ivanov on 5/4/18.
 */
@SuppressWarnings("CanBeFinal")
@RunWith(AndroidJUnit4.class)
public class PhoneRecipeMasterDetailsFlowTest {

    private static final String RECIPE_TITLE = "Nutella Pie";
    private static final String STEP_DESCRIPTION = "Step 1: Starting prep";
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @Before. The activity will be terminated after
     * the test and methods annotated with @After are complete. This rule allows you to directly
     * access the activity during the test.
     */
    @Rule
    public ActivityTestRule<ActivityRecipesList> mActivityTestRule = new ActivityTestRule<>(ActivityRecipesList.class);

    @Test
    public void check_isRecipeList_Displayed() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.layout_recipe_collection)).check(matches(isDisplayed()));
    }

    /**
     * Clicks on a Recipe List and checks it opens up the Details Activity correct data.
     * <p>
     * https://android.googlesource.com/platform/frameworks/testing/+/61a929bd4642b9042bfb05b85340c1761ab90733/espresso/espresso-lib-tests/src/androidTest/java/com/google/android/apps/common/testing/ui/espresso/action/SwipeActionIntegrationTest.java
     * Test ViewPager - credits to https://stackoverflow.com/questions/29836405/testing-viewpager-with-espresso-how-perfom-action-to-a-button-of-an-item
     */
    @Test
    public void clickRecipeViewItem_OpensDetailsActivity() {


        onView(withId(R.id.layout_recipe_collection))
                .perform(actionOnItemAtPosition(0, click()));

        //Check if the selected item matches the toolbar text
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(RECIPE_TITLE)));

        onView(allOf(withId(R.id.rv_baking_steps), withParent(withId(R.id.layout_baking_steps_collection))))
                .perform(actionOnItemAtPosition(0, click()));


        // Check View pager navigation
        onView(withId(R.id.vp_baking_steps)).check(matches(isDisplayed()));
        onView(withId(R.id.vp_baking_steps)).perform(swipeLeft());

        onView(CoreMatchers.allOf(withId(R.id.tv_step_short_description), isDescendantOfA(withId(R.id.vp_baking_steps)),isDisplayed()))
                .check(matches(withText(STEP_DESCRIPTION)));

        // Check ingredients are properly displayed
        onView(withId(R.id.layout_ingredients)).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.btn_toggle_ingredients), withParent(withId(R.id.layout_ingredients))))
                .perform(click());

        onView(withId(R.id.pageIndicatorView)).check(matches(isDisplayed()));


        onView(withId(R.id.rv_ingredients_list)).check(matches(isDisplayed()));
        onView(withId(R.id.layout_ingredients)).perform(swipeDown());

        onView(withId(R.id.tv_header_ingredients)).check(matches(isDisplayingAtLeast(20)));


    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
