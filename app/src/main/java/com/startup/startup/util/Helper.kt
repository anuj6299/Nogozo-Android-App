package com.startup.startup.util

import android.text.TextUtils
import android.util.Patterns


class Helper {
    companion object{
        fun isValidEmail(target: String): Boolean {
            return if (TextUtils.isEmpty(target)) {
                false
            } else {
                Patterns.EMAIL_ADDRESS.matcher(target).matches()
            }
        }

        fun isPhoneNumber(string: String): Boolean{
            if(string.length != 10)
                return false
            return !Patterns.PHONE.matcher(string).matches();
        }
    }
}