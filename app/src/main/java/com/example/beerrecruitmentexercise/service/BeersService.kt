package com.example.beerrecruitmentexercise.service

import com.example.beerrecruitmentexercise.dto.BeerDTO

import java.util.ArrayList

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface BeersService {

    /**
     * Methos to GET the data from API
     *
     * @return the beers list
     */
    @GET(URL_BEERS)
    fun getBeersData(@Query("page") page: String,
                     @Query("food") food: String?): Observable<ArrayList<BeerDTO>>

    companion object {

        const val URL_BEERS = "/v2/beers"
    }
}
