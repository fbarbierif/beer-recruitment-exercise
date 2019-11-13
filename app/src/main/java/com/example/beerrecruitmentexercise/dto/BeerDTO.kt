package com.example.beerrecruitmentexercise.dto

import com.google.gson.annotations.SerializedName

import io.realm.RealmList
import io.realm.RealmModel
import io.realm.annotations.RealmClass

@RealmClass
open class BeerDTO : RealmModel {

    var key = ""
    private var id: Int = 0
    var name: String = ""
    var tagline: String = ""
    @SerializedName("first_brewed")
    var firstBrewed: String = ""
    var description: String = ""
    @SerializedName("image_url")
    var imageUrl: String = ""
    var abv: Float = 0.0f
    var ibu: Float = 0.0f
    @SerializedName("target_fg")
    var targetFg: Float = 0.0f
    @SerializedName("target_og")
    var targetOg: Float = 0.0f
    var ebc: Float = 0.0f
    var srm: Float = 0.0f
    var ph: Float = 0.0f
    @SerializedName("attenuation_level")
    var attenuationLevel: Float = 0.0f
    @SerializedName("food_pairing")
    var foodPairing: RealmList<String>? = null
    @SerializedName("brewers_tips")
    var brewersTips: String = ""
    @SerializedName("contributed_by")
    var contributedBy: String = ""

    /**
     * Beer constructor
     *
     * @param key              the beer key
     * @param id               the beer id
     * @param name             the beer name
     * @param tagline          the beer tagline
     * @param firstBrewed      the beer first brewed date
     * @param description      the beer description
     * @param imageUrl         the beer image to show
     * @param abv              the beer abv
     * @param ibu              the beer ibu
     * @param targetFg         the beer target Fg
     * @param targetOg         the beer target Og
     * @param ebc              the beer ebc
     * @param srm              the beer srm
     * @param ph               the beer ph
     * @param attenuationLevel the beer attenuation level
     * @param foodPairing      the beer food pairing
     * @param brewersTips      the brewer tips
     * @param contributedBy    the contribution to the beer
     */
    constructor(key: String, id: Int, name: String, tagline: String, firstBrewed: String, description: String,
                imageUrl: String, abv: Float, ibu: Float, targetFg: Float, targetOg: Float, ebc: Float,
                srm: Float, ph: Float, attenuationLevel: Float,
                foodPairing: RealmList<String>, brewersTips: String, contributedBy: String, id1: Int) {
        this.key = key
        this.id = id
        this.name = name
        this.tagline = tagline
        this.firstBrewed = firstBrewed
        this.description = description
        this.imageUrl = imageUrl
        this.abv = abv
        this.ibu = ibu
        this.targetFg = targetFg
        this.targetOg = targetOg
        this.ebc = ebc
        this.srm = srm
        this.ph = ph
        this.attenuationLevel = attenuationLevel
        this.foodPairing = foodPairing
        this.brewersTips = brewersTips
        this.contributedBy = contributedBy
        this.id = id1
    }

    constructor()
}
