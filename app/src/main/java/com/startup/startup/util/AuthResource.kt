package com.startup.startup.util

import androidx.annotation.NonNull
import androidx.annotation.Nullable

class AuthResource<T> {

    @NonNull
    var Status: AuthStatus

    @Nullable
    var data: T

    @Nullable
    var message: String

    constructor(@Nullable Status: AuthStatus, @Nullable data: T, @Nullable message: String){
        this.Status =Status
        this.data = data
        this.message = message
    }

    enum class AuthStatus{NOT_AUTHENTICATED, AUTHENTICATED, ERROR, LOADING}

    companion object {
        public fun <T> authenticated(data: T): AuthResource<T> {
            return AuthResource(AuthStatus.AUTHENTICATED, data, "AUTHENTICATED")
        }

        public fun <T> error(msg: String, data: T): AuthResource<T> {
            return AuthResource(AuthStatus.ERROR, data, msg)
        }

        public fun <T> loading(): AuthResource<T> {
            return AuthResource(AuthStatus.LOADING, null as T, "LOADING")
        }

        public fun <T> notAuthenticated(data: T): AuthResource<T> {
            return AuthResource(AuthStatus.NOT_AUTHENTICATED, data, "NOT AUTHENTICATED")
        }
    }
}