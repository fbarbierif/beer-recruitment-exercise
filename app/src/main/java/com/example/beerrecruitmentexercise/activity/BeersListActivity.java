package com.example.beerrecruitmentexercise.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.beerrecruitmentexercise.R;
import com.example.beerrecruitmentexercise.adapter.BeersAdapter;
import com.example.beerrecruitmentexercise.dto.BeerDTO;
import com.example.beerrecruitmentexercise.presenter.BeersPresenter;
import com.example.beerrecruitmentexercise.utils.EndlessRecyclerViewScrollListener;
import com.example.beerrecruitmentexercise.view.BeersView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.beerrecruitmentexercise.utils.RealmUtils.deleteFromRealmWithoutKey;
import static com.example.beerrecruitmentexercise.utils.RealmUtils.isSearchStoredInDB;
import static com.example.beerrecruitmentexercise.utils.RealmUtils.restoreSearchFromDB;
import static com.example.beerrecruitmentexercise.utils.RealmUtils.storeBeers;

public class BeersListActivity extends AppCompatActivity implements BeersView {

    private BeersPresenter beersPresenter;
    private BeersAdapter beersAdapter;
    RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout llErrorEmptyView;
    private TextView tvMessage;
    private SwipeRefreshLayout srLayout;
    EditText etSearch;
    ImageView ivClear;
    ArrayList<BeerDTO> beers = new ArrayList<>();
    String food = null;
    private EndlessRecyclerViewScrollListener listener;

    private static final int FIRST_PAGE = 1;
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String UNDERSCORE = "_";
    public static final String ASCENDING = "ascending";
    public static final String DESCENDING = "descending";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beers_list);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.rvBeers);
        etSearch = findViewById(R.id.etSearch);
        ivClear = findViewById(R.id.ivClear);
        llErrorEmptyView = findViewById(R.id.llErrorEmptyView);
        tvMessage = findViewById(R.id.tvMessage);
        srLayout = findViewById(R.id.swipeRefreshLt);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        beersPresenter = new BeersPresenter(this);

        listener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page, final int totalItemsCount,
                                   final RecyclerView recyclerView) {
                if (food == null) {
                    beersPresenter.getBeersData(String.valueOf(page), food);
                }
            }
        };

        recyclerView.addOnScrollListener(listener);

        srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetSearch();
                beersPresenter.getBeersData(String.valueOf(FIRST_PAGE), food);
                recyclerView.addOnScrollListener(listener);
            }
        });

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    food = getFoodFormatted(etSearch.getText().toString());
                    if (!food.isEmpty()) {
                        executeSearch();
                    }
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                resetSearch();
            }
        });
    }

    /**
     * Execute the search
     */
    private void executeSearch() {
        showProgressBar();
        beers.clear();
        ivClear.setVisibility(View.VISIBLE);
        food = getFoodFormatted(etSearch.getText().toString());
        searchInDBorAPI(food);
    }

    @Override
    public void onBackPressed() {
        if (food != null) {
            if(!food.isEmpty()){
                resetSearch();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressBar();
        beersPresenter.getBeersData(String.valueOf(FIRST_PAGE), food);
    }

    /**
     * Search by key if object was stored in db and get it or make api call if necesary
     *
     * @param food the string as key to search object
     */
    private void searchInDBorAPI(final String food) {
        if (isSearchStoredInDB(food)) {
            beers.clear();
            ArrayList<BeerDTO> restoredBeers = restoreSearchFromDB(food);
            if(restoredBeers == null || restoredBeers.isEmpty()){
                showEmptyView();
            } else  {
                showBeers(restoredBeers);
            }

        } else {
            beersPresenter.getBeersData(String.valueOf(FIRST_PAGE), food);
        }

    }

    /**
     * Format the search string to use it correctly
     *
     * @param food the string to format
     * @return the formatted string
     */
    private String getFoodFormatted(String food) {
        final String formattedFood;
        formattedFood = food.trim().replace(SPACE, UNDERSCORE);
        return formattedFood.toLowerCase();
    }

    /**
     * Hide the soft keyboard
     */
    private void hideKeyboard() {
        final InputMethodManager imm =
                (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Sort and show the data
     *
     * @param beersResult the data received
     */
    @Override
    public void sortAndShowBeersData(ArrayList<BeerDTO> beersResult) {
        recyclerView.setVisibility(View.VISIBLE);
        hideKeyboard();
        showBeers(beersResult);
    }

    /**
     * Display beers in recyclerview
     *
     * @param beersResult the data to display
     */
    private void showBeers(ArrayList<BeerDTO> beersResult) {

        beers.addAll(beersResult);

        if (beersAdapter == null) {
            beersAdapter = new BeersAdapter(beers);
            recyclerView.setAdapter(beersAdapter);
        } else {
            beersAdapter.notifyDataSetChanged();
        }

        srLayout.setRefreshing(false);
        hideProgressBar();

        deleteFromRealmWithoutKey();

        if (food != null) {
            storeBeers(beersResult, food);
        } else {
            storeBeers(beersResult, null);
        }
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
        showErrorEmptyLayout(getString(R.string.error));
    }

    @Override
    public void showEmptyView() {
        showErrorEmptyLayout(getString(R.string.empty));
    }

    /**
     * Show error or empty view
     *
     * @param message the message to display
     */
    private void showErrorEmptyLayout(final String message) {
        recyclerView.setVisibility(View.GONE);
        hideKeyboard();
        if (beersAdapter != null) {
            beersAdapter.notifyDataSetChanged();
        }
        srLayout.setRefreshing(false);
        tvMessage.setText(message);
        llErrorEmptyView.setVisibility(View.VISIBLE);
    }

    /**
     * Reset search box and make request to show initial data
     */
    public void resetSearch() {
        llErrorEmptyView.setVisibility(View.INVISIBLE);
        ivClear.setVisibility(View.INVISIBLE);
        food = null;
        beers.clear();
        etSearch.clearFocus();
        etSearch.setText(EMPTY);
        hideKeyboard();
        showProgressBar();
        beersPresenter.getBeersData(String.valueOf(FIRST_PAGE), food);
        recyclerView.addOnScrollListener(listener);
    }

}
