package com.example.beerrecruitmentexercise.dto;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class MashTempItemDTO implements RealmModel {

    ValueUnitDTO temp;
    private float duration;

    /**
     * Constructor
     *
     * @param temp the mash item temperature
     * @param duration the mash item duration
     */
    public MashTempItemDTO(ValueUnitDTO temp, float duration) {
        this.temp = temp;
        this.duration = duration;
    }

    public MashTempItemDTO(){}

    public ValueUnitDTO getTemp() {
        return temp;
    }

    public float getDuration() {
        return duration;
    }
}
