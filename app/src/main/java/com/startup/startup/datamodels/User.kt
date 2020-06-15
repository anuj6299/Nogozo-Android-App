package com.startup.startup.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("userid")
    @Expose
    var userId: String = "-1"

    @SerializedName("usertype")
    @Expose
    var userType: String = "-1"

    @SerializedName("name")
    @Expose
    var name: String = "-1"

    @SerializedName("profilelevel")
    @Expose
    var profileLevel: String  = "-1"

    constructor(userId: String, userType: String, name: String, profileLevel: String){
        this.userId = userId
        this.userType = userType
        this.name = name
        this.profileLevel = profileLevel
    }

    constructor()
}