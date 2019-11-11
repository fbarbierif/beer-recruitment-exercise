package com.example.beerrecruitmentexercise.service;

import com.example.beerrecruitmentexercise.dto.BeerDTO;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface BeersService {

    String URL_BEERS = "/v2/beers";

    /**
     * Methos to GET the data from API
     *
     * @return the beers list
     */
    @GET(URL_BEERS)
    Observable<ArrayList<BeerDTO>> getBeersData(@Query("page") String page,
                                                @Query("food") String food);
}
