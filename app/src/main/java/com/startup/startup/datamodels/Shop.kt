package com.startup.startup.datamodels

class Shop {
    var shopName: String? = null

    lateinit var shopId: String

    var imageUrl: String? = null

    var shopAddress: String? = null

    var shopCurrentStatus: String? = null

    constructor(shopName: String?, shopId: String, imageUrl: String?, shopCurrentStatus: String?){
        this.shopId = shopId
        this.shopName = shopName
        this.imageUrl = imageUrl
        this.shopCurrentStatus = shopCurrentStatus
    }

    constructor()
}