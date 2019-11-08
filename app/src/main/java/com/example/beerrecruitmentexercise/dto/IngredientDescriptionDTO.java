package com.example.beerrecruitmentexercise.dto;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
class IngredientDescriptionDTO implements RealmModel {

    private String name;
    private ValueUnitDTO amount;
    private String add;
    private String attribute;

    /**
     * Ingredients description constructor
     *
     * @param name the ingredient name
     * @param amount the ingredient amount
     * @param add the ingredient add
     * @param attribute the ingredient attribute
     */
    public IngredientDescriptionDTO(String name, ValueUnitDTO amount, String add, String attribute) {
        this.name = name;
        this.amount = amount;
        this.add = add;
        this.attribute = attribute;
    }

    public String getName() {
        return name;
    }

    public ValueUnitDTO getAmount() {
        return amount;
    }

    public String getAdd() {
        return add;
    }

    public String getAttribute() {
        return attribute;
    }
}
