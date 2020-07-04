package com.startup.startup.datamodels

import java.io.Serializable

data class Item(
    var itemId: String? = "-1",
    var itemName: String? = "-1",
    var itemPrice: String = "0",
    var itemQuantity: String? = "0",
    var itemImageUrl: String? = "",
    var isAvailable: Boolean? = true
): Serializable {

    constructor(itemId: String) : this() {
        this.itemId = itemId
    }

    fun equalsTo(other: Any?): Boolean{
        if(other !is Item){
            val o = other as Item
            if(o.itemId == this.itemId
                && o.itemName == this.itemName
                && o.itemPrice == this.itemPrice
                && o.itemQuantity == this.itemQuantity
                && o.isAvailable == this.isAvailable)
                return true
        }
        return false
    }
}