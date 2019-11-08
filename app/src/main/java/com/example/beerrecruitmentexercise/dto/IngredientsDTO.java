package com.example.beerrecruitmentexercise.dto;

import java.util.ArrayList;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
class IngredientsDTO implements RealmModel {

    private ArrayList<IngredientDescriptionDTO> malt;
    private ArrayList<IngredientDescriptionDTO> hops;
    private String yeast;

    /**
     * Ingredients constructor
     *
     * @param malt the malt ingredients
     * @param hops the hops ingredients
     * @param yeast the yeast
     */
    public IngredientsDTO(ArrayList<IngredientDescriptionDTO> malt,
                          ArrayList<IngredientDescriptionDTO> hops, String yeast) {
        this.malt = malt;
        this.hops = hops;
        this.yeast = yeast;
    }

    public ArrayList<IngredientDescriptionDTO> getMalt() {
        return malt;
    }

    public ArrayList<IngredientDescriptionDTO> getHops() {
        return hops;
    }

    public String getYeast() {
        return yeast;
    }
}
