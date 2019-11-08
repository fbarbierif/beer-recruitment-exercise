package com.example.beerrecruitmentexercise.dto;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class IngredientsDTO implements RealmModel {

    private RealmList<IngredientDescriptionDTO> malt;
    private RealmList<IngredientDescriptionDTO> hops;
    private String yeast;

    /**
     * Ingredients constructor
     *
     * @param malt the malt ingredients
     * @param hops the hops ingredients
     * @param yeast the yeast
     */
    public IngredientsDTO(RealmList<IngredientDescriptionDTO> malt,
                          RealmList<IngredientDescriptionDTO> hops, String yeast) {
        this.malt = malt;
        this.hops = hops;
        this.yeast = yeast;
    }

    public IngredientsDTO(){}

    public RealmList<IngredientDescriptionDTO> getMalt() {
        return malt;
    }

    public RealmList<IngredientDescriptionDTO> getHops() {
        return hops;
    }

    public String getYeast() {
        return yeast;
    }
}
