package com.example.android_task.utils

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenProvider @Inject constructor()  {
    private var accessToken: String? = null

    fun getToken(): String? {
        return accessToken
    }

    fun setToken(token: String) {
        accessToken = token
    }
}