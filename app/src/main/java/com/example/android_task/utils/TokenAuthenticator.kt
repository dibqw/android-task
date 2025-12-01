package com.example.android_task.utils

import com.example.android_task.data.entity.LoginRequest
import com.example.android_task.data.services.ApiServices
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class TokenAuthenticator : Authenticator {

    // A separate, clean OkHttpClient instance just for the synchronous login call.
    // It must NOT have this Authenticator attached to avoid a loop.
    private val loginClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    private val loginRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.baubuddy.de/dev/")
        .client(loginClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val loginService: ApiServices = loginRetrofit.create(ApiServices::class.java)

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
                    AuthTokenProvider.accessToken = newAccessToken
                    return newAccessToken
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}