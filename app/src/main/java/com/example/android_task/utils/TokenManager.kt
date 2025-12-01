package com.example.android_task.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class TokenManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("api_prefs", Context.MODE_PRIVATE)
    private val ACCESS_TOKEN_KEY = "access_token"

    fun saveToken(token: String) {
        prefs.edit { putString(ACCESS_TOKEN_KEY, token) }
    }

    fun fetchToken(): String? {
        return prefs.getString(ACCESS_TOKEN_KEY, null)
    }

    fun deleteToken() {
        prefs.edit { remove(ACCESS_TOKEN_KEY) }
    }
}
