package com.startup.startup.datamodels

class Shop {
    lateinit var shopName: String

    lateinit var shopId: String

    lateinit var imageUrl: String

    constructor(shopName: String, shopId: String, imageUrl: String){
        this.shopId = shopId
        this.shopName = shopName
        this.imageUrl = imageUrl
    }

    constructor()
}