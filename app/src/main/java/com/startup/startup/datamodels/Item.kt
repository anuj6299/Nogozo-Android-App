package com.startup.startup.datamodels

data class Item(
    var itemId: String? = "-1",
    var itemName: String? = "-1",
    var itemPrice: String? = "-1",
    var itemQuantity: String? = "-1",
    var isAvailable: Boolean? = true
) {

    constructor(itemId: String) : this() {
        this.itemId = itemId
    }
}