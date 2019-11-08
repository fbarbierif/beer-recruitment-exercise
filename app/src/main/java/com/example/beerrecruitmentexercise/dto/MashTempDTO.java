package com.example.beerrecruitmentexercise.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
class MashTempDTO implements RealmModel {

    @SerializedName("mash_temp")
    private ArrayList<MashTempItemDTO> mashTemp;

    /**
     * Constructor
     *
     * @param mashTemp the mash temp
     */
    public MashTempDTO(ArrayList<MashTempItemDTO> mashTemp) {
        this.mashTemp = mashTemp;
    }

    public ArrayList<MashTempItemDTO> getMashTemp() {
        return mashTemp;
    }
}
