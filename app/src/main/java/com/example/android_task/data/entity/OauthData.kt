package com.example.android_task.data.entity

import com.google.gson.annotations.SerializedName

data class OauthData(
    @SerializedName("access_token")
    val accessToken: String
)

