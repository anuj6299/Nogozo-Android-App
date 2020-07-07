package com.startup.startup.datamodels

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Order(
    var orderId: String = "",
    var items: HashMap<String, Any> = HashMap(),
    var date: String = "-1",
    var time: String = "-1",
    var datetime: String = "-1",
    var status: String = "-1",
    var price: String = "0",
    var shopid: String = "-1",
    var shopname: String = "",
    var customername: String = "-1",
    var customeraddress: String = "-1",
    var customerphone: String = "-1"
){
    //constructor() : this() {}
}