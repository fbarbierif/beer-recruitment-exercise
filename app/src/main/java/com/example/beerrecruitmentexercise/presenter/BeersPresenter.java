package com.example.beerrecruitmentexercise.presenter;

import android.util.Log;

import com.example.beerrecruitmentexercise.dto.BeerDTO;
import com.example.beerrecruitmentexercise.model.BeersModel;
import com.example.beerrecruitmentexercise.view.BeersView;

import java.util.ArrayList;
import java.util.Objects;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BeersPresenter {

    private final BeersView beersView;
    private static final String REQUEST_ERROR = "Request error";

    /**
     * Presenter constructor
     *
     * @param view the view interface to communicate with the activity
     */
    public BeersPresenter(final BeersView view) {
        beersView = view;
    }

    /**
     * Begins de API call to get the beers data
     *
     * @param page the number of the page to request
     * @param food the string (if user searched) or null
     */
    public void getBeersData(final String page, final String food) {

        final Observable<ArrayList<BeerDTO>> observable =
                BeersModel.getInstance().getBeersData(page, food);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ArrayList<BeerDTO>>() {
                    @Override
                    public void onCompleted() {
                        //Nothing to do
                    }

                    @Override
                    public void onError(final Throwable e) {
                        beersView.hideProgressBar();
                        beersView.showErrorView();
                        Log.e(REQUEST_ERROR, Objects.requireNonNull(e.getMessage()));
                    }

                    @Override
                    public void onNext(final ArrayList<BeerDTO> result) {
                        if (result == null || result.isEmpty()) {
                            beersView.hideProgressBar();
                            beersView.showEmptyView();
                        } else {
                            beersView.sortAndShowBeersData(result);
                        }
                    }
                });
    }
}
