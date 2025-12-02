package com.example.android_task.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android_task.data.dao.SelectTaskDAO
import com.example.android_task.data.entity.SelectTask

@Database(entities = [SelectTask::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): SelectTaskDAO
}