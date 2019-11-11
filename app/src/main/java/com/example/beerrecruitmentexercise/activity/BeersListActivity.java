package com.example.beerrecruitmentexercise.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
import com.example.beerrecruitmentexercise.dto.SearchDTO;
import com.example.beerrecruitmentexercise.presenter.BeersPresenter;
import com.example.beerrecruitmentexercise.utils.EndlessRecyclerViewScrollListener;
import com.example.beerrecruitmentexercise.view.BeersView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

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
    final ArrayList<BeerDTO> beers = new ArrayList<>();
    String food = null;
    private EndlessRecyclerViewScrollListener listener;

    private static final int FIRST_PAGE = 1;
    public static final String EMPTY = "";
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
                beers.clear();
                srLayout.setRefreshing(true);
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
                        beers.clear();
                        food = getFoodFormatted(etSearch.getText().toString());
                        ivClear.setVisibility(View.VISIBLE);

                        searchInDBorAPI(food);
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

    @Override
    public void onBackPressed() {
        if (!food.isEmpty()) {
            resetSearch();
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.menu_sort_descending) {
            sortStoredBeersByABV(DESCENDING);
        } else {
            sortStoredBeersByABV(ASCENDING);
        }
        return true;
    }

    /**
     * Search by key if object was stored in db and get it or make api call if necesary
     *
     * @param food the string as key to search object
     */
    private void searchInDBorAPI(final String food) {

        if (isSearchStoredInDB(food)) {
            restoreSearchFromDB(food);
        } else {
            beersPresenter.getBeersData(String.valueOf(FIRST_PAGE), food);
        }

    }

    /**
     * Retrieve the object stores in db by the key
     *
     * @param food the string as key to search object
     */
    private void restoreSearchFromDB(final String food) {
        final Realm realm = Realm.getDefaultInstance();
        SearchDTO search = realm.where(SearchDTO.class)
                .equalTo("key", food)
                .findFirst();

        beers.clear();

        ArrayList<BeerDTO> restoredBeers = new ArrayList<>();
        restoredBeers.addAll(search.getBeers());
        showBeers(restoredBeers);

    }

    /**
     * Search by key if object was stored in db or not
     *
     * @param food the string as key to search object
     */
    private boolean isSearchStoredInDB(final String food) {
        final Realm realm = Realm.getDefaultInstance();
        SearchDTO search = realm.where(SearchDTO.class)
                .equalTo("key", food)
                .findFirst();

        return search != null;
    }

    /**
     * Format the search string to use it correctly
     *
     * @param food the string to format
     * @return the formatted string
     */
    private String getFoodFormatted(String food) {
        final String formattedFood;
        formattedFood = food.trim().replace(" ", "_");
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
        llErrorEmptyView.setVisibility(View.GONE);

        sortBeersAscending(beersResult);

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

        deleteAllFromRealm();

        if (food != null) {
            storeBeersByKey(beersResult);
        } else {
            storeBeers(beersResult);
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
        ivClear.setVisibility(View.INVISIBLE);
        food = null;
        beers.clear();
        etSearch.clearFocus();
        etSearch.setText(EMPTY);
        hideKeyboard();
        showProgressBar();
        beersPresenter.getBeersData(String.valueOf(FIRST_PAGE), food);
    }

    /**
     * Sort initial beers data ascending
     *
     * @param beersResult the beers to order
     */
    private void sortBeersAscending(ArrayList<BeerDTO> beersResult) {
        Collections.sort(beersResult, new Comparator<BeerDTO>() {
            @Override
            public int compare(BeerDTO beer1, BeerDTO beer2) {
                return Float.compare(beer1.getAbv(), beer2.getAbv());
            }
        });
    }

    /**
     * Delete all stored beers in db
     */
    private void deleteAllFromRealm() {

        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(BeerDTO.class);
            }
        });
    }

    /**
     * Sort the stored beers by param (ascending/descending)
     *
     * @param order the order to sort
     */
    private void sortStoredBeersByABV(final String order) {

        Sort realmOrder;
        if (ASCENDING.equalsIgnoreCase(order)) {
            realmOrder = Sort.ASCENDING;
        } else {
            realmOrder = Sort.DESCENDING;
        }

        final Realm realm = Realm.getDefaultInstance();
        RealmResults<BeerDTO> beersSorted = realm.where(BeerDTO.class)
                .sort("abv", realmOrder)
                .findAll();

        final ArrayList<BeerDTO> beersList = new ArrayList<>();

        beers.clear();
        beersList.addAll(realm.copyFromRealm(beersSorted));

        showBeers(beersList);

        realm.close();
    }

    /**
     * Store beers in db
     *
     * @param beersList the beers to store
     */
    public void storeBeers(final ArrayList<BeerDTO> beersList) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmList<BeerDTO> beersListToStore = new RealmList<>();
                    beersListToStore.addAll(beersList);
                    realm.insertOrUpdate(beersListToStore);
                }
            });
        } catch (Exception e) {
            Log.d("Realm error", e.getMessage());
        }
    }

    /**
     * Store searched object (key/beers)
     *
     * @param beersList the beers to store
     */
    public void storeBeersByKey(final ArrayList<BeerDTO> beersList) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmList<BeerDTO> beersListToStore = new RealmList<>();
                    beersListToStore.addAll(beersList);
                    SearchDTO searchObject = new SearchDTO(food, beersListToStore);
                    realm.insertOrUpdate(searchObject);
                }
            });
        } catch (Exception e) {
            Log.d("Realm error", e.getMessage());
        }
    }
}
