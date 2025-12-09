package com.example.android_task.utils

import android.util.Log
import com.example.android_task.data.entity.LoginRequest
import com.example.android_task.data.services.ApiServices
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val loginService: ApiServices,
    private val tokenProvider: AuthTokenProvider
) : Authenticator {

    // This runs synchronously when a 401 is received
    override fun authenticate(route: Route?, response: Response): Request? {
        // Check if we have already tried to refresh the token and failed
        if (response.request.header("Authorization") != null) {
            // Token refresh failed or invalid credentials, manual logout might be needed here
            return null
        }

        // Use 'synchronized' to ensure only one thread is trying to refresh the token at a time
        synchronized(this) {
            // Attempt to refresh the token synchronously
            val newToken = refreshAccessToken()

            return if (newToken != null) {
                // If successful, create a new request with the new Bearer token
                response.request.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
            } else {
                // Refresh failed, return null to signal OkHttp to give up and propagate the 401
                null
            }
        }
    }

    // This function MUST be synchronous (blocking)
    private fun refreshAccessToken(): String? {
        try {
            val loginRequest = LoginRequest("365", "1")
            // This is a blocking call (using .execute() on Retrofit Call)
            val loginCall = loginService.loginSynchronous(loginRequest)
            val loginResponse = loginCall.execute()

            if (loginResponse.isSuccessful) {
                val newAccessToken = loginResponse.body()?.oauth?.accessToken
                if (newAccessToken != null) {
                    // Update our singleton provider with the new token
                    tokenProvider.setToken(newAccessToken)
                    Log.e("Authenticator", "New token is generated")
                    return newAccessToken
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}