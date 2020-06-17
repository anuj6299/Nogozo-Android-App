package com.startup.startup.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ListOfArea {
    @SerializedName("id")
    @Expose
    lateinit var cityId: String

    @SerializedName("areas")
    @Expose
    lateinit var areas: List<Area>

    constructor(cityId: String, areas: List<Area>){
        this.cityId = cityId
        this.areas = areas
    }

    constructor()
}