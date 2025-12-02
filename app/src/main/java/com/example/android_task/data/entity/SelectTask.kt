package com.example.android_task.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "tasks")
data class SelectTask(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "task")
    @SerializedName("task")
        val task: String,
    @ColumnInfo(name = "title")@SerializedName("title") val title: String,
    @ColumnInfo(name = "description")@SerializedName("description") val description: String,
    @ColumnInfo(name = "colorCode")@SerializedName("colorCode") val colorCode: String
)
