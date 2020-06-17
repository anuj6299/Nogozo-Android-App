package com.startup.startup.ui.main.customer

import androidx.annotation.NonNull
import androidx.annotation.Nullable

class ServiceResource<T> {

    @Nullable
    var status: Status

    @Nullable
    var data: T

    @Nullable
    var message: String

    constructor(@NonNull Status: Status, @Nullable data: T, @Nullable message: String){
        this.status =Status
        this.data = data
        this.message = message
    }

    companion object {
        fun <T> success(data: T): ServiceResource<T> {
            return ServiceResource(Status.SUCCESS, data, "success")
        }

        fun <T> error(msg: String): ServiceResource<T> {
            return ServiceResource(Status.ERROR, null as T, msg)
        }

        fun <T> loading(): ServiceResource<T> {
            return ServiceResource(Status.LOADING, null as T, "loading")
        }
    }
    enum class Status{SUCCESS, ERROR, LOADING}
}