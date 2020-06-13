package com.startup.startup.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("userid")
    @Expose
    private lateinit var userId: String

    @SerializedName("usertype")
    @Expose
    private lateinit var userType: String

    @SerializedName("name")
    @Expose
    private lateinit var name: String

    constructor(userId: String, userType: String, name: String){
        this.userId = userId
        this.userType = userType
        this.name = name
    }

    constructor()
}