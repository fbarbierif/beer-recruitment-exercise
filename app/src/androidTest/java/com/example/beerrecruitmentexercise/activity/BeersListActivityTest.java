package com.example.beerrecruitmentexercise.activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.beerrecruitmentexercise.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class BeersListActivityTest {

    @Rule
    public ActivityTestRule<BeersListActivity> activityActivityTestRule =
            new ActivityTestRule<>(BeersListActivity.class);

    @Test
    public void TestShowRecyclerView () {
        onView(withId(R.id.rvBeers)).check(matches(isDisplayed()));
    }

}