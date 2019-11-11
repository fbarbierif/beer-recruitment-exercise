package com.example.beerrecruitmentexercise.dto;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class SearchDTO implements RealmModel {

    private String key;
    private RealmList<BeerDTO> beers = new RealmList<>();

    /**
     * Beer search result constructor
     *
     * @param key the key of the search
     * @param beers the list of beers
     */
    public SearchDTO(String key, RealmList<BeerDTO> beers) {
        this.key = key;
        this.beers = beers;
    }

    public SearchDTO(){}

    public String getKey() {
        return key;
    }

    public RealmList<BeerDTO> getBeers() {
        return beers;
    }
}
