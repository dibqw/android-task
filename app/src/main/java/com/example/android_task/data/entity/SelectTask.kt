package com.example.android_task.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "tasks")
data class SelectTask(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    val task: String,
    val title: String,
    val description: String,
    val sort: Int,
    val wageType: String,
    @ColumnInfo(name = "businessUnitKey")
    @SerializedName("BusinessUnitKey")
    val businessUnitKey: String?,
    val businessUnit: String,
    val parentTaskID: String,
    val preplanningBoardQuickSelect: String?,
    val colorCode: String,
    val workingTime: String?,
    val isAvailableInTimeTrackingKioskMode: Boolean,
    val isAbstract: Boolean,
    val externalId: Int?



//"sort": "0",
//"BusinessUnitKey": "Gerüstbau",
//"businessUnit": "Gerüstbau",
//"parentTaskID": "",
//"preplanningBoardQuickSelect": null,
//"colorCode": "",
//"workingTime": null,
//"isAvailableInTimeTrackingKioskMode": false,
//"isAbstract": false,
//"externalId": null

)
