package com.example.beerrecruitmentexercise.presenter

import android.util.Log

import com.example.beerrecruitmentexercise.dto.BeerDTO
import com.example.beerrecruitmentexercise.model.BeersModel
import com.example.beerrecruitmentexercise.view.BeersView

import java.util.ArrayList
import java.util.Objects

import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class BeersPresenter
/**
 * Presenter constructor
 *
 * @param view the view interface to communicate with the activity
 */
(private val beersView: BeersView) {

    /**
     * Begins de API call to get the beers data
     *
     * @param page the number of the page to request
     * @param food the string (if user searched) or null
     */
    fun getBeersData(page: String, food: String?) {

        val observable = BeersModel.instance.getBeersData(page, food)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<ArrayList<BeerDTO>>() {
                    override fun onCompleted() {
                        //Nothing to do
                    }

                    override fun onError(e: Throwable) {
                        beersView.hideProgressBar()
                        beersView.showErrorView()
                        Log.e(REQUEST_ERROR, Objects.requireNonNull<String>(e.message))
                    }

                    override fun onNext(result: ArrayList<BeerDTO>?) {
                        if (result == null || result.isEmpty()) {
                            beersView.hideProgressBar()
                            beersView.showEmptyView()
                        } else {
                            beersView.sortAndShowBeersData(result)
                        }
                    }
                })
    }

    companion object {
        private val REQUEST_ERROR = "Request error"
    }
}
