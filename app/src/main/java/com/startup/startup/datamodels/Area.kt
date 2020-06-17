package com.startup.startup.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Area {
    @SerializedName("areaname")
    @Expose
    lateinit var areaName: String

    @SerializedName("areaid")
    @Expose
    lateinit var areaId: String

    constructor(areaName: String, areaId: String){
        this.areaId = areaId
        this.areaName = areaName
    }

    constructor()
}