package com.example.android_task.data.services

import com.example.android_task.data.entity.LoginRequest
import com.example.android_task.data.entity.LoginResponse
import com.example.android_task.data.entity.SelectTask
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiServices {

    // For synchronous use in the Authenticator (returns OkHttp Call, NO 'suspend')
    @POST("index.php/login")
    @Headers("Authorization: Basic QVBJX0V4cGxvcmVyOjEyMzQ1NmlzQUxhbWVQYXNz")
    fun loginSynchronous(@Body request: LoginRequest): Call<LoginResponse>


    @GET("index.php/v1/tasks/select")
    suspend fun getAllTasks() : Response<List<SelectTask>>
}