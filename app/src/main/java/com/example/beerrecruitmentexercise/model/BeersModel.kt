package com.example.beerrecruitmentexercise.model

import com.example.beerrecruitmentexercise.dto.BeerDTO
import com.example.beerrecruitmentexercise.service.BeersService

import java.util.ArrayList

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable

class BeersModel {

    /**
     * Instance Retrofit and create service to communicate with API
     *
     * @return the service created.
     */
    private val service: BeersService
        get() {
            val retrofitBuilder = Retrofit.Builder().baseUrl(BASE_URL)
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
            retrofitBuilder.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            return retrofitBuilder.build().create(BeersService::class.java)
        }

    /**
     * Call the endpoint to retrieve the beers list
     *
     * @return the response containing the beers
     */
    fun getBeersData(page: String, food: String?): Observable<ArrayList<BeerDTO>> {
        return service.getBeersData(page, food)
    }

    companion object {

        /**
         * Factory method
         *
         * @return the model's singleton
         */
        val instance = BeersModel()
        private val BASE_URL = "https://api.punkapi.com/"
    }

}
