package com.example.beerrecruitmentexercise.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.beerrecruitmentexercise.R;
import com.example.beerrecruitmentexercise.dto.BeerDTO;
import com.example.beerrecruitmentexercise.presenter.BeersPresenter;
import com.example.beerrecruitmentexercise.view.BeersView;

import java.util.ArrayList;

public class BeersListActivity extends AppCompatActivity implements BeersView {

    private BeersPresenter beersPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beers_list);

        beersPresenter = new BeersPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        beersPresenter.getBeersData();
    }

    @Override
    public void showBeersData(ArrayList<BeerDTO> beers) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void showEmptyView() {

    }
}
