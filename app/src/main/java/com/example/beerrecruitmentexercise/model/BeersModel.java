package com.example.beerrecruitmentexercise.model;

import com.example.beerrecruitmentexercise.dto.BeerDTO;
import com.example.beerrecruitmentexercise.service.BeersService;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class BeersModel {

    private static final BeersModel INSTANCE = new BeersModel();
    private static final String BASE_URL = "https://api.punkapi.com/";

    /**
     * Factory method
     *
     * @return the model's singleton
     */
    public static BeersModel getInstance() {
        return INSTANCE;
    }

    /**
     * Instance Retrofit and create service to communicate with API
     *
     * @return the service created.
     */
    private BeersService getService() {
        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BASE_URL);
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        retrofitBuilder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return retrofitBuilder.build().create(BeersService.class);
    }

    /**
     * Call the endpoint to retrieve the beers list
     *
     * @return the response containing the beers
     */
    public Observable<ArrayList<BeerDTO>> getBeersData(final String page, final String food) {
        return getService().getBeersData(page, food);
    }

}
