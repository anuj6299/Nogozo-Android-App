package com.startup.startup.network

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.startup.startup.datamodels.CustomerProfile

class Database {


    fun uploadToken(token: String){
        FirebaseDatabase.getInstance().reference.child("token")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(token)
    }

    fun getUserProfile(userType: String): DatabaseReference{

        return FirebaseDatabase.getInstance().reference
            .child("users").child(userType)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    fun setUserProfile(userType: String, map: HashMap<String, Any>): Task<Void> {
        val ref = FirebaseDatabase.getInstance().reference
            .child("users").child(userType)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        return ref.updateChildren(map)
    }

    fun setUserProfileOnRegistered(userType: String, profile: CustomerProfile){
        val ref = FirebaseDatabase.getInstance().reference
            .child("users").child(userType)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        val map: HashMap<String, Any> = HashMap()
        map["email"] = profile.email!!
        map["profilelevel"] = profile.profilelevel!!

        ref.updateChildren(map)
    }

    fun getCities(): DatabaseReference{
        return FirebaseDatabase.getInstance().reference
            .child("citylist")
    }

    fun getAreas(cityId: String): DatabaseReference{
        return FirebaseDatabase.getInstance().reference
            .child("arealist").child(cityId)
    }

    fun getServices(): DatabaseReference{
        return FirebaseDatabase.getInstance().reference
            .child("service")
    }

    fun getShops(serviceId: String, areaId: String): DatabaseReference{
        return FirebaseDatabase.getInstance().reference
            .child("shops").child(serviceId).child(areaId)
    }

    fun getItems(shopId: String): DatabaseReference{
        return FirebaseDatabase.getInstance().reference
            .child("items").child(shopId)
    }
}