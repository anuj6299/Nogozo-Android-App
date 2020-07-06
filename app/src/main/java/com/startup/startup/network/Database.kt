package com.startup.startup.network

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
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
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("profile")
    }

    fun setUserProfile(userType: String, map: HashMap<String, Any>): Task<Void> {
        val ref = FirebaseDatabase.getInstance().reference
            .child("users").child(userType)
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("profile")

        return ref.updateChildren(map)
    }

    fun setUserProfileOnRegistered(userType: String, profile: CustomerProfile){
        val ref = FirebaseDatabase.getInstance().reference
            .child("users").child(userType)
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("profile")

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

    fun getItemById(shopId: String, itemId: String): DatabaseReference{
        return FirebaseDatabase.getInstance().reference
            .child("items").child(shopId).child(itemId)
    }

    fun changeItemAvailabilityStatus(shopId: String, itemId: String, itemAvailabilityStatus: String){
        FirebaseDatabase.getInstance().reference
            .child("items").child(shopId).child(itemId)
            .child("isAvailable").setValue(itemAvailabilityStatus)
    }

    fun getShopAddress(shopid: String): DatabaseReference{
        return FirebaseDatabase.getInstance().reference
            .child("users").child("shop")
            .child(shopid).child("address")
    }

    fun getShopStatus(shopId: String): DatabaseReference{
        return FirebaseDatabase.getInstance().reference
            .child("users").child("shop")
            .child(shopId).child("status")
    }


    fun createOrder(): DatabaseReference{
        return FirebaseDatabase.getInstance().reference
            .child("orders").push()
    }

    fun getCurrentOrders(userType: String,userId: String): Query{
        return FirebaseDatabase.getInstance().reference
            .child("users").child(userType)
            .child(userId).child("orders").orderByValue().equalTo("current")
    }

    fun getPastOrder(userType: String,userId: String): Query{
        return FirebaseDatabase.getInstance().reference
            .child("users").child(userType)
            .child(userId).child("orders").orderByValue().equalTo("history")
    }

    fun getOrderDetails(orderId: String): DatabaseReference{
        return FirebaseDatabase.getInstance().reference
            .child("orders").child(orderId)
    }

    fun createItemInShop(shopId: String, map: HashMap<String, Any>, imagePath: Uri? = null): Task<Void> {
        val itemid =  FirebaseDatabase.getInstance().reference
            .child("items").child(shopId).push().key!!

        if(imagePath != null){
            FirebaseStorage.getInstance().reference
                .child("items").child(itemid).putFile(imagePath)
        }

        return FirebaseDatabase.getInstance().reference
            .child("items").child(shopId).child(itemid).setValue(map)
    }

    fun editItemInShop(shopId: String, itemId: String, map: HashMap<String, Any>, imagePath: Uri? = null): Task<Void>{
        val ref = FirebaseDatabase.getInstance().reference
            .child("items").child(shopId).child(itemId)

        if(imagePath != null){
            FirebaseStorage.getInstance().reference
                .child("items").child(itemId).putFile(imagePath)
        }

        return ref.updateChildren(map)
    }

    fun markedOrderPacked(orderId: String, status: String): Task<Void>{
        return FirebaseDatabase.getInstance().reference
            .child("orders").child(orderId)
            .child("status").setValue(status)
    }
}