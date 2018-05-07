package com.bakingstory;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.bakingstory.RecipeCollection.ActivityRecipesList;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

/**
 * Created by emil.ivanov on 5/4/18.
 */
@RunWith(AndroidJUnit4.class)
public class PhoneRecipeMasterDetailsFlowTest {

    public static final String RECIPE_TITILE = "Brownies";

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

        onView(withId(R.id.layout_recipe_collection)).check(matches(isDisplayed()));
    }

    /**
     * Clicks on a Recipe List and checks it opens up the Details Activity correct data.
     */
    @Test
    public void clickRecipeViewItem_OpensDetailsActivity() {


        onView(withId(R.id.layout_recipe_collection))
                .perform(actionOnItemAtPosition(1, click()));

        //Check if the selected item matches the toolbar text
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(RECIPE_TITILE)));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
