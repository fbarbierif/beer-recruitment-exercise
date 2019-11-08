package com.example.beerrecruitmentexercise.dto;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
class FermentationDTO implements RealmModel {

    private ValueUnitDTO temp;

    /**
     * Fermentation constructor
     *
     * @param temp the fermentation temperature
     */
    public FermentationDTO(ValueUnitDTO temp) {
        this.temp = temp;
    }

    public ValueUnitDTO getTemp() {
        return temp;
    }
}
