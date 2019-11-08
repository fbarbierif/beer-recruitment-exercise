package com.example.beerrecruitmentexercise.dto;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class BeerDTO implements RealmModel {

    private int id;
    private String name;
    private String tagline;
    @SerializedName("first_brewed")
    private String firstBrewed;
    private String description;
    @SerializedName("image_url")
    private String imageUrl;
    private float abv;
    private float ibu;
    @SerializedName("target_fg")
    private float targetFg;
    @SerializedName("target_og")
    private float targetOg;
    private float ebc;
    private float srm;
    private float ph;
    @SerializedName("attenuation_level")
    private float attenuationLevel;
    private ValueUnitDTO volume;
    @SerializedName("boil_volume")
    private ValueUnitDTO boilVolume;
    private MethodDTO method;
    private IngredientsDTO ingredients;
    @SerializedName("food_pairing")
    private RealmList<String> foodPairing;
    @SerializedName("brewers_tips")
    private String brewersTips;
    @SerializedName("contributed_by")
    private String contributedBy;

    /**
     * Beer constructor
     *
     * @param id the beer id
     * @param name the beer name
     * @param tagline the beer tagline
     * @param firstBrewed the beer first brewed date
     * @param description the beer description
     * @param imageUrl the beer image to show
     * @param abv the beer abv
     * @param ibu the beer ibu
     * @param targetFg the beer target Fg
     * @param targetOg the beer target Og
     * @param ebc the beer ebc
     * @param srm the beer srm
     * @param ph the beer ph
     * @param attenuationLevel the beer attenuation level
     * @param volume the beer volume
     * @param boilVolume the beer boil volume
     * @param method the beer production method
     * @param ingredients the beer ingredients
     * @param foodPairing the beer food pairing
     * @param brewersTips the brewer tips
     * @param contributedBy the contribution to the beer
     */
    public BeerDTO(int id, String name, String tagline, String firstBrewed, String description,
                   String imageUrl, float abv, float ibu, float targetFg, float targetOg, float ebc,
                   float srm, float ph, float attenuationLevel, ValueUnitDTO volume,
                   ValueUnitDTO boilVolume, MethodDTO method, IngredientsDTO ingredients,
                   RealmList<String> foodPairing, String brewersTips, String contributedBy) {
        this.id = id;
        this.name = name;
        this.tagline = tagline;
        this.firstBrewed = firstBrewed;
        this.description = description;
        this.imageUrl = imageUrl;
        this.abv = abv;
        this.ibu = ibu;
        this.targetFg = targetFg;
        this.targetOg = targetOg;
        this.ebc = ebc;
        this.srm = srm;
        this.ph = ph;
        this.attenuationLevel = attenuationLevel;
        this.volume = volume;
        this.boilVolume = boilVolume;
        this.method = method;
        this.ingredients = ingredients;
        this.foodPairing= foodPairing;
        this.brewersTips = brewersTips;
        this.contributedBy = contributedBy;
    }

    public BeerDTO(){}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }

    public String getFirstBrewed() {
        return firstBrewed;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public float getAbv() {
        return abv;
    }

    public float getIbu() {
        return ibu;
    }

    public float getTargetFg() {
        return targetFg;
    }

    public float getTargetOg() {
        return targetOg;
    }

    public float getEbc() {
        return ebc;
    }

    public float getSrm() {
        return srm;
    }

    public float getPh() {
        return ph;
    }

    public float getAttenuationLevel() {
        return attenuationLevel;
    }

    public ValueUnitDTO getVolume() {
        return volume;
    }

    public ValueUnitDTO getBoilVolume() {
        return boilVolume;
    }

    public MethodDTO getMethod() {
        return method;
    }

    public IngredientsDTO getIngredients() {
        return ingredients;
    }

    public RealmList<String> getFoodPairing() {
        return foodPairing;
    }

    public String getBrewersTips() {
        return brewersTips;
    }

    public String getContributedBy() {
        return contributedBy;
    }
}
