package com.example.beerrecruitmentexercise.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.os.Bundle;
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

public class BeersListActivity extends AppCompatActivity implements BeersView {

    private BeersPresenter beersPresenter;
    private BeersAdapter beersAdapter;
    RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;
    private LinearLayout llErrorEmptyView;
    private TextView tvMessage;
    private SwipeRefreshLayout srLayout;
    EditText etSearch;
    ImageView ivClear;
    final ArrayList<BeerDTO> beers = new ArrayList<>();

    public static final String EMPTY = "";

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
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        beersPresenter = new BeersPresenter(this);

        srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                beers.clear();
                beersPresenter.getBeersData();
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
        beersPresenter.getBeersData();
    }

    private void hideKeyboard() {
        final InputMethodManager imm =
                (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void showBeersData(ArrayList<BeerDTO> beersResult) {
        recyclerView.setVisibility(View.VISIBLE);
        hideKeyboard();
        llErrorEmptyView.setVisibility(View.GONE);
        beers.addAll(beersResult);

        if (beersAdapter == null) {
            beersAdapter = new BeersAdapter(beers, getApplicationContext());
            recyclerView.setAdapter(beersAdapter);
        } else {
            beersAdapter.notifyDataSetChanged();
        }

        srLayout.setRefreshing(false);
        hideProgressBar();
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
        beersAdapter.notifyDataSetChanged();
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
        beersPresenter.getBeersData();
    }
}
