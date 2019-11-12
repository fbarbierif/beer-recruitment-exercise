package com.example.beerrecruitmentexercise.dto;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class SearchDTO implements RealmModel {

    @PrimaryKey
    private String key;
    BeerDTO beer;

    /**
     * Beer search result constructor
     *
     * @param key   the key of the search
     * @param beer the beer
     */
    public SearchDTO(String key, BeerDTO beer) {
        this.key = key;
        this.beer = beer;
    }

    public SearchDTO() {
    }

    public String getKey() {
        return key;
    }

    public BeerDTO getBeer() {
        return beer;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setBeers(BeerDTO beer) {
        this.beer = beer;
    }
}


