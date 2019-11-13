package com.example.beerrecruitmentexercise.utils

import android.content.Context

import com.example.beerrecruitmentexercise.dto.BeerDTO

import java.util.ArrayList

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.RealmResults

object RealmUtils {

    private val KEY = "key"

    /**
     * Realm initializer
     *
     * @param context the context
     */
    fun initializeRealm(context: Context) {
        Realm.init(context)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }

    /**
     * Search by key if object was stored in db or not
     *
     * @param food the string as key to search object
     */
    fun isSearchStoredInDB(food: String): Boolean {
        val realm = Realm.getDefaultInstance()
        val search = realm.where(BeerDTO::class.java)
                .equalTo(KEY, food)
                .findAll()
        return search != null && !search.isEmpty()
    }

    /**
     * Delete all stored beers in db
     */
    fun deleteFromRealmWithoutKey() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm ->
            val result = realm.where(BeerDTO::class.java).equalTo(KEY, "").findAll()
            result.deleteAllFromRealm()
        }
        realm.close()
    }

    /**
     * Store searched object (key/beers)
     *
     * @param beersList the beers to store
     * @param food      the food to use as key
     */
    fun storeBeers(beersList: ArrayList<BeerDTO>, food: String?) {
        if (food != null) {
            for (beer in beersList) {
                beer.key = food
            }
        }
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm ->
            val beersListToStore = RealmList<BeerDTO>()
            beersListToStore.addAll(beersList)
            realm.insertOrUpdate(beersListToStore)
        }
        realm.close()
    }

    /**
     * Retrieve the object stores in db by the key
     *
     * @param food the string as key to search object
     */
    fun restoreSearchFromDB(food: String): ArrayList<BeerDTO> {
        val realm = Realm.getDefaultInstance()

        val search = realm.where(BeerDTO::class.java)
                .equalTo(KEY, food)
                .findAll()

        val results = ArrayList<BeerDTO>()
        results.addAll(realm.copyFromRealm(search))

        realm.close()
        return results
    }

}
