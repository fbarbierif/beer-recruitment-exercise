package com.example.beerrecruitmentexercise.dto;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
class ValueUnitDTO implements RealmModel {

    private float value;
    private String unit;

    /**
     * ValueUnit constructor
     *
     * @param value the value
     * @param unit the unit of the value
     */
    public ValueUnitDTO(float value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public float getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}
