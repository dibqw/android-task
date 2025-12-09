package com.example.android_task.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android_task.data.entity.SelectTask
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectTaskDAO {
    @Query("SELECT * FROM tasks")
    fun getAll(): Flow<List<SelectTask>>

    @Insert
    fun insertAll(tasksList: List<SelectTask>)
}