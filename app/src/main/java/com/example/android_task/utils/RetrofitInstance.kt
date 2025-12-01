package com.example.android_task.utils

import com.example.android_task.data.services.ApiServices
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://api.baubuddy.de/dev/"

//    private val authInterceptor = AuthInterceptor()

//    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(authInterceptor)
//        .connectTimeout(30, TimeUnit.SECONDS)
//        .readTimeout(30, TimeUnit.SECONDS)
//        .build()

    private val tokenAuthenticator = TokenAuthenticator()

    private val okHttpClient = OkHttpClient.Builder()
        .authenticator(tokenAuthenticator) // <-- Attach the authenticator here
        .build()
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiServices by lazy {
        retrofit.create(ApiServices::class.java)
    }
}