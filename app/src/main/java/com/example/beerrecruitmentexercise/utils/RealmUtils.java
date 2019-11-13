package com.example.beerrecruitmentexercise.utils;

import android.content.Context;

import com.example.beerrecruitmentexercise.dto.BeerDTO;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public abstract class RealmUtils {

    private static final String KEY = "key";

    /**
     * Realm initializer
     *
     * @param context the context
     */
    public static void initializeRealm(final Context context) {
        Realm.init(context);
        final RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    /**
     * Search by key if object was stored in db or not
     *
     * @param food the string as key to search object
     */
    public static boolean isSearchStoredInDB(final String food) {
        final Realm realm = Realm.getDefaultInstance();
        RealmResults search = realm.where(BeerDTO.class)
                .equalTo(KEY, food)
                .findAll();
        return search != null && !search.isEmpty();
    }

    /**
     * Delete all stored beers in db
     */
    public static void deleteFromRealmWithoutKey() {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<BeerDTO> result = realm.where(BeerDTO.class).equalTo(KEY, "").findAll();
                result.deleteAllFromRealm();
            }
        });
        realm.close();
    }

    /**
     * Store searched object (key/beers)
     *
     * @param beersList the beers to store
     * @param food      the food to use as key
     */
    public static void storeBeers(final ArrayList<BeerDTO> beersList, final String food) {
        if (food != null) {
            for (BeerDTO beer : beersList) {
                beer.setKey(food);
            }
        }
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmList<BeerDTO> beersListToStore = new RealmList<>();
                beersListToStore.addAll(beersList);
                realm.insertOrUpdate(beersListToStore);
            }
        });
        realm.close();
    }

    /**
     * Retrieve the object stores in db by the key
     *
     * @param food the string as key to search object
     */
    public static ArrayList<BeerDTO> restoreSearchFromDB(final String food) {
        final Realm realm = Realm.getDefaultInstance();

        RealmResults search = realm.where(BeerDTO.class)
                .equalTo(KEY, food)
                .findAll();

        ArrayList<BeerDTO> results = new ArrayList<>();
        results.addAll(realm.copyFromRealm(search));

        realm.close();
        return results;
    }

}
