package com.example.beerrecruitmentexercise.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.beerrecruitmentexercise.R;
import com.example.beerrecruitmentexercise.adapter.BeersAdapter;
import com.example.beerrecruitmentexercise.dto.BeerDTO;
import com.example.beerrecruitmentexercise.presenter.BeersPresenter;
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

        srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                beers.clear();
                srLayout.setRefreshing(true);
                beersPresenter.getBeersData(String.valueOf(1), null);
            }
        });

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //TODO FB
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
    protected void onResume() {
        super.onResume();

        showProgressBar();
        beersPresenter.getBeersData(String.valueOf(1), null);
    }

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

    @Override
    public void showBeersData(ArrayList<BeerDTO> beersResult) {
        recyclerView.setVisibility(View.VISIBLE);
        hideKeyboard();
        llErrorEmptyView.setVisibility(View.GONE);

        sortBeersAscending(beersResult);

        beers.clear();
        beers.addAll(beersResult);

        if (beersAdapter == null) {
            beersAdapter = new BeersAdapter(beers, getApplicationContext());
            recyclerView.setAdapter(beersAdapter);
        } else {
            beersAdapter.notifyDataSetChanged();
        }

        srLayout.setRefreshing(false);
        hideProgressBar();

        deleteAllFromRealm();
        storeBeers(beersResult);
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

    private void showErrorEmptyLayout(final String message) {
        recyclerView.setVisibility(View.GONE);
        hideKeyboard();
        if(beersAdapter != null ){
            beersAdapter.notifyDataSetChanged();
        }
        srLayout.setRefreshing(false);
        tvMessage.setText(message);
        llErrorEmptyView.setVisibility(View.VISIBLE);
    }

    public void resetSearch() {
        beers.clear();
        etSearch.clearFocus();
        etSearch.setText(EMPTY);
        hideKeyboard();
        showProgressBar();
        beersPresenter.getBeersData(String.valueOf(1), null);
    }

    private void sortBeersAscending(ArrayList<BeerDTO> beersResult) {
        Collections.sort(beersResult, new Comparator<BeerDTO>() {
            @Override
            public int compare(BeerDTO beer1, BeerDTO beer2) {
                return Float.compare(beer1.getAbv(), beer2.getAbv());
            }
        });
    }

    private void deleteAllFromRealm() {

        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(BeerDTO.class);
            }
        });
    }

    private void sortStoredBeersByABV(final String order) {

        Sort realmOrder;
        if(ASCENDING.equalsIgnoreCase(order)){
            realmOrder = Sort.ASCENDING;
        }else{
            realmOrder = Sort.DESCENDING;
        }

        final Realm realm = Realm.getDefaultInstance();
        RealmResults<BeerDTO> beersSorted = realm.where(BeerDTO.class)
                              .sort("abv", realmOrder)
                              .findAll();

        final ArrayList<BeerDTO> beers = new ArrayList<>();
        beers.addAll(realm.copyFromRealm(beersSorted));

        showBeersData(beers);

        realm.close();
    }

    public void storeBeers(final ArrayList<BeerDTO> beersList) {
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmList<BeerDTO> beersListToStore = new RealmList<>();
                    beersListToStore.addAll(beersList);
                    realm.insertOrUpdate(beersListToStore);
                }
            });
        } catch (Exception e){
            Log.d("Realm error", e.getMessage());
        }
    }
}
