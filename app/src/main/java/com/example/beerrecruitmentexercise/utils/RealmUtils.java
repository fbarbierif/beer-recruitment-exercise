package com.example.beerrecruitmentexercise.utils;

import android.content.Context;

import com.example.beerrecruitmentexercise.dto.BeerDTO;
import com.example.beerrecruitmentexercise.dto.SearchDTO;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public abstract class RealmUtils {

    private static final String ASCENDING = "ascending";
    private static final String DESCENDING = "descending";
    private static final String ABV = "abv";
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
     * Retrieve the object stores in db by the key
     *
     * @param food the string as key to search object
     */
    public static ArrayList<BeerDTO> restoreSearchFromDB(final String food) {
        final Realm realm = Realm.getDefaultInstance();
        SearchDTO search = realm.where(SearchDTO.class)
                .equalTo(KEY, food)
                .findFirst();

        ArrayList<BeerDTO> restoredBeers = new ArrayList<>();
        if (search != null) {
            restoredBeers.addAll(search.getBeers());
        }
        realm.close();
        return restoredBeers;
    }

    /**
     * Search by key if object was stored in db or not
     *
     * @param food the string as key to search object
     */
    public static boolean isSearchStoredInDB(final String food) {
        final Realm realm = Realm.getDefaultInstance();
        SearchDTO search = realm.where(SearchDTO.class)
                .equalTo(KEY, food)
                .findFirst();
        realm.close();
        return search != null;
    }

    /**
     * Delete all stored beers in db
     */
    public static void deleteAllFromRealm() {

        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(BeerDTO.class);
            }
        });
        realm.close();
    }

    /**
     * Sort the stored beers by param (ascending/descending)
     *
     * @param order the order to sort
     * @return the beers sorted
     */
    public static ArrayList<BeerDTO> sortStoredBeersByABV(final String order) {

        Sort realmOrder = null;
        if (ASCENDING.equalsIgnoreCase(order)) {
            realmOrder = Sort.ASCENDING;
        } else if (DESCENDING.equalsIgnoreCase(order)) {
            realmOrder = Sort.DESCENDING;
        }

        final Realm realm = Realm.getDefaultInstance();
        RealmResults<BeerDTO> beersSorted = null;
        if (realmOrder != null) {
            beersSorted = realm.where(BeerDTO.class)
                    .sort(ABV, realmOrder)
                    .findAll();
        }

        final ArrayList<BeerDTO> beersList = new ArrayList<>();
        beersList.addAll(realm.copyFromRealm(beersSorted));

        realm.close();

        return beersList;
    }

    /**
     * Store beers in db
     *
     * @param beersList the beers to store
     */
    public static void storeBeers(final ArrayList<BeerDTO> beersList) {
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
     * Store searched object (key/beers)
     *
     * @param beersList the beers to store
     */
    public static void storeBeersByKey(final ArrayList<BeerDTO> beersList, final String food) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmList<BeerDTO> beersListToStore = new RealmList<>();
                beersListToStore.addAll(beersList);
                SearchDTO searchObject = new SearchDTO(food, beersListToStore);
                realm.insertOrUpdate(searchObject);
            }
        });
        realm.close();
    }

}
