package com.startup.startup.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class City {
    @SerializedName("cityname")
    @Expose
    private lateinit var cityName: String

    @SerializedName("cityid")
    @Expose
    private lateinit var cityId: String

    constructor(cityName: String, cityId: String){
        this.cityId = cityId
        this.cityName = cityName
    }

    constructor()

}