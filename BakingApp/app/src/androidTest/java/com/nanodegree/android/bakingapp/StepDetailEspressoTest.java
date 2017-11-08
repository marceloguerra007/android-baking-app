package com.nanodegree.android.bakingapp;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.nanodegree.android.bakingapp.model.Step;
import com.nanodegree.android.bakingapp.utilities.NetworkUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by marceloguerra on 20/10/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StepDetailEspressoTest {

    private static final int STEP_ID_A = 1;
    private static final int STEP_ID_B = 2;
    private static final int STEP_ID_C = 3;
    private static final String STEP_DESCRIPTION_A = "Step A";
    private static final String STEP_DESCRIPTION_B = "Step B";
    private static final String STEP_DESCRIPTION_C = "Step C";
    private static final String STEP_INSTRUCTION_A = "Instructions of Step A.";
    private static final String STEP_INSTRUCTION_B = "Instructions of Step B.";
    private static final String STEP_INSTRUCTION_C = "Instructions of Step C.";
    private static final String STEP_VIDEOURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";

    private ArrayList<Step> stepsTest;

    @Rule
    public FragmentTestRule<?, StepDetailFragment> fragmentTestRule =
            FragmentTestRule.create(StepDetailFragment.class);

    @Before
    public void setSteps(){
        stepsTest = new ArrayList<Step>();
        stepsTest.add(new Step(STEP_ID_A, STEP_DESCRIPTION_A, STEP_INSTRUCTION_A, ""));
        stepsTest.add(new Step(STEP_ID_B, STEP_DESCRIPTION_B, STEP_INSTRUCTION_B, STEP_VIDEOURL));
        stepsTest.add(new Step(STEP_ID_C, STEP_DESCRIPTION_C, STEP_INSTRUCTION_C, ""));
    }

    @Test
    public void click_NextStep() throws Exception {
        fragmentTestRule.getFragment().refreshContent(0, stepsTest);

        onView(withId(R.id.Bt_NextStep)).perform(click());

        onView(withId(R.id.TV_StepInstruction)).check(matches(withText(STEP_INSTRUCTION_B)));
    }

    @Test
    public void click_PreviousStep() throws Exception {
        fragmentTestRule.getFragment().refreshContent(2, stepsTest);

        onView(withId(R.id.Bt_PreviousStep)).perform(click());

        onView(withId(R.id.TV_StepInstruction)).check(matches(withText(STEP_INSTRUCTION_B)));
    }

    @Test
    public void check_NoVideoAvailable(){
        fragmentTestRule.getFragment().refreshContent(1, stepsTest);

        onView(withId(R.id.Bt_PreviousStep)).perform(click());

        onView(withId(R.id.CV_NoVideo)).check(matches(isDisplayed()));
    }

    @Test
    public void check_VideoAvailable(){
        fragmentTestRule.getFragment().refreshContent(0, stepsTest);

        onView(withId(R.id.Bt_NextStep)).perform(click());

        if (NetworkUtil.existsInternetConnection(fragmentTestRule.getFragment().getContext()))
            onView(withId(R.id.CV_NoVideo)).check(matches(not(isDisplayed())));
        else
            onView(withId(R.id.CV_NoVideo)).check(matches(isDisplayed()));
    }
}
