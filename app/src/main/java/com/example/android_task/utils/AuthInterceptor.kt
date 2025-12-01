package com.example.android_task.utils

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = AuthTokenProvider.accessToken

        // Add the Bearer token if it exists (i.e., after login)
        val requestBuilder = originalRequest.newBuilder().apply {
            if (accessToken != null) {
                header("Authorization", "Bearer $accessToken")
            }
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
