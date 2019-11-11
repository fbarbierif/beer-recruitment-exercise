package com.example.beerrecruitmentexercise.view;

import com.example.beerrecruitmentexercise.dto.BeerDTO;

import java.util.ArrayList;

public interface BeersView {

    void sortAndShowBeersData(ArrayList<BeerDTO> beers);

    void showProgressBar();

    void hideProgressBar();

    void showErrorView();

    void showEmptyView();
}
