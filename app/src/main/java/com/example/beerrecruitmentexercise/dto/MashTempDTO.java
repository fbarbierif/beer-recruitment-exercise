package com.example.beerrecruitmentexercise.dto;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class MashTempDTO implements RealmModel {

    @SerializedName("mash_temp")
    private RealmList<MashTempItemDTO> mashTemp;

    /**
     * Constructor
     *
     * @param mashTemp the mash temp
     */
    public MashTempDTO(RealmList<MashTempItemDTO> mashTemp) {
        this.mashTemp = mashTemp;
    }

    public MashTempDTO() {
    }

    public RealmList<MashTempItemDTO> getMashTemp() {
        return mashTemp;
    }
}
