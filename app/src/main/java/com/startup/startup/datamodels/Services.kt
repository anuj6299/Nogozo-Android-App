package com.startup.startup.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Services {
    @SerializedName("servicename")
    @Expose
    lateinit var serviceName: String

    @SerializedName("serviceid")
    @Expose
    lateinit var serviceId: String

    @SerializedName("serviceimageurl")
    @Expose
    lateinit var imageUrl: String

    constructor(serviceName: String, serviceId: String, imageUrl: String){
        this.serviceId = serviceId
        this.serviceName = serviceName
        this.imageUrl = imageUrl
    }

    constructor()
}