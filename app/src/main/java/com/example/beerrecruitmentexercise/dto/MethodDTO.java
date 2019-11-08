package com.example.beerrecruitmentexercise.dto;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class MethodDTO implements RealmModel {

    @SerializedName("mash_temp")
    private RealmList<MashTempDTO> mashTemp;
    private FermentationDTO fermentation;
    private String twist;

    /**
     * Method constructor
     *
     * @param mashTemp the mash temp
     * @param fermentation the fermentation
     * @param twist the twist
     */
    public MethodDTO(RealmList<MashTempDTO> mashTemp, FermentationDTO fermentation, String twist) {
        this.mashTemp = mashTemp;
        this.fermentation = fermentation;
        this.twist = twist;
    }

    public MethodDTO(){}

    public RealmList<MashTempDTO> getMashTemp() {
        return mashTemp;
    }

    public FermentationDTO getFermentation() {
        return fermentation;
    }

    public String getTwist() {
        return twist;
    }
}
