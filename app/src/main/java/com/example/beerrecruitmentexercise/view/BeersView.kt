package com.example.beerrecruitmentexercise.view

import com.example.beerrecruitmentexercise.dto.BeerDTO

import java.util.ArrayList

interface BeersView {

    /**
     * Order and display the data received from API on main screen
     *
     * @param beers The beers to order and display
     */
    fun sortAndShowBeersData(beers: ArrayList<BeerDTO>)

    /**
     * Show the progress bar on main screen
     */
    fun showProgressBar()

    /**
     * Hide the progress bar on main screen
     */
    fun hideProgressBar()

    /**
     * Show the error view on main screen when api call fails
     */
    fun showErrorView()

    /**
     * Show the empty view on main screen when api call returns empty results
     */
    fun showEmptyView()
}
