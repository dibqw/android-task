package com.example.android_task.data.entity

import com.google.gson.annotations.SerializedName

data class SelectTask(
    @SerializedName("task") val task: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("colorCode") val colorCode: String
)
