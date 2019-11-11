package com.example.beerrecruitmentexercise.model;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.beerrecruitmentexercise.activity.BeersListActivity;
import com.example.beerrecruitmentexercise.dto.BeerDTO;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class BeersModelTest {

    private BeersModel beersModel;
    private static final String FIRST_PAGE = "1";
    private static final String EMPTY = "";
    private static final int PAGE_ITEMS_COUNT = 25;

    @Rule
    public ActivityTestRule<BeersListActivity> activityActivityTestRule =
            new ActivityTestRule<>(BeersListActivity.class);

    @Before
    public void setUp() {
        beersModel = new BeersModel();
    }

    @Test
    public void TestGetBeersCompleted() {
        beersModel.getBeersData(FIRST_PAGE,null).test().assertCompleted();
    }

    @Test
    public void TestGetRecipesListCount() {
        final TestSubscriber<ArrayList<BeerDTO>> subscriber = TestSubscriber.create();
        beersModel.getBeersData(FIRST_PAGE,null).subscribe(subscriber);
        subscriber.assertNoErrors();
        subscriber.assertCompleted();
        assertThat(subscriber.getOnNextEvents().get(0).size(), is(PAGE_ITEMS_COUNT));
    }

}