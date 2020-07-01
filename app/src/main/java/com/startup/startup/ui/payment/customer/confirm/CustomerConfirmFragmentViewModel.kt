package com.startup.startup.ui.payment.customer.confirm

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.startup.startup.SessionManager
import javax.inject.Inject

class CustomerConfirmFragmentViewModel
    @Inject
    constructor(
        private val sessionManager: SessionManager
    ): ViewModel() {

    fun getUserAddress(): String{
        return sessionManager.getUserAddress()
    }
    fun getUserPhone(): String{
        return sessionManager.getUserPhone()
    }
    fun getUserName(): String{
        return sessionManager.getUserName()
    }
    fun getUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}