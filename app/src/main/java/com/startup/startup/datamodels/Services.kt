package com.startup.startup.datamodels

class Services {
    lateinit var serviceName: String

    lateinit var serviceId: String

    var imageUrl: String? = null

    constructor(serviceId: String, serviceName: String, imageUrl: String?){
        this.serviceId = serviceId
        this.serviceName = serviceName
        this.imageUrl = imageUrl
    }

    constructor()
}