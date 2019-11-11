package com.example.beerrecruitmentexercise.view;

import com.example.beerrecruitmentexercise.dto.BeerDTO;

import java.util.ArrayList;

public interface BeersView {

    /**
     * Order and display the data received from API on main screen
     *
     * @param beers The beers to order and display
     */
    void sortAndShowBeersData(ArrayList<BeerDTO> beers);

    /**
     * Show the progress bar on main screen
     */
    void showProgressBar();

    /**
     * Hide the progress bar on main screen
     */
    void hideProgressBar();

    /**
     * Show the error view on main screen when api call fails
     */
    void showErrorView();

    /**
     * Show the empty view on main screen when api call returns empty results
     */
    void showEmptyView();
}
