package com.startup.startup.ui.auth

import androidx.annotation.NonNull
import androidx.annotation.Nullable

class AuthResource<T> {

    @NonNull
    var Status: AuthStatus

    @Nullable
    var data: T

    @Nullable
    var message: String

    constructor(@NonNull Status: AuthStatus, @Nullable data: T, @Nullable message: String){
        this.Status =Status
        this.data = data
        this.message = message
    }

    enum class AuthStatus{NOT_AUTHENTICATED, AUTHENTICATED, ERROR, LOADING}

    companion object {
        fun <T> authenticated(data: T): AuthResource<T> {
            return AuthResource(
                AuthStatus.AUTHENTICATED,
                data,
                "AUTHENTICATED"
            )
        }

        fun <T> error(msg: String, data: T): AuthResource<T> {
            return AuthResource(
                AuthStatus.ERROR,
                data,
                msg
            )
        }

        fun <T> loading(): AuthResource<T> {
            return AuthResource(
                AuthStatus.LOADING,
                null as T,
                "LOADING"
            )
        }

        fun <T> notAuthenticated(): AuthResource<T> {
            return AuthResource(
                AuthStatus.NOT_AUTHENTICATED,
                null as T,
                "NOT AUTHENTICATED"
            )
        }
    }
}