package com.example.beerrecruitmentexercise.dto;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
class MethodDTO implements RealmModel {

    @SerializedName("mash_temp")
    private MashTempDTO mashTemp;
    private FermentationDTO fermentation;
    private String twist;

    /**
     * Method constructor
     *
     * @param mashTemp the mash temp
     * @param fermentation the fermentation
     * @param twist the twist
     */
    public MethodDTO(MashTempDTO mashTemp, FermentationDTO fermentation, String twist) {
        this.mashTemp = mashTemp;
        this.fermentation = fermentation;
        this.twist = twist;
    }

    public MashTempDTO getMashTemp() {
        return mashTemp;
    }

    public FermentationDTO getFermentation() {
        return fermentation;
    }

    public String getTwist() {
        return twist;
    }
}
