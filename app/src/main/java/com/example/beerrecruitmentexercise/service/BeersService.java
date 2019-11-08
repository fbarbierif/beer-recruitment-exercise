package com.example.beerrecruitmentexercise.service;

import com.example.beerrecruitmentexercise.dto.BeerDTO;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface BeersService {

    String URL_BEERS = "/v2/beers";

    /**
     * @return the beers list
     */
    @GET(URL_BEERS)
    Observable<ArrayList<BeerDTO>> getBeersData();
}
