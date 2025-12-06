package com.example.android_task.data.repos

import android.util.Log
import com.example.android_task.data.dao.SelectTaskDAO
import com.example.android_task.data.entity.LoginRequest
import com.example.android_task.data.entity.SelectTask
import com.example.android_task.data.services.ApiServices
import com.example.android_task.utils.AuthTokenProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class TasksRepo @Inject constructor(
    private val taskDao: SelectTaskDAO,
    private val apiService: ApiServices
) {
    // The stream of data from the database
    suspend fun getTasks(): List<SelectTask> {
        return withContext(Dispatchers.IO) {
            taskDao.getAll()
        }
    }

    suspend fun getTasksByQuery(query: String): List<SelectTask> {
        return withContext(Dispatchers.IO) {
            taskDao.findByQuery(query)
        }
    }

    suspend fun refreshTasks() {
        withContext(Dispatchers.IO) { // Perform network and DB operations on IO dispatcher
            try {
                val remoteTasksResponse = apiService.getAllTasks() // Fetches from API
                if (remoteTasksResponse.isSuccessful) {
                    taskDao.insertAll(remoteTasksResponse.body()!!) // Inserts into DB, which updates the Flow
                } else Log.e("RepoGetTasks", "Request failed with code: ${remoteTasksResponse.code()}")
            } catch (e: IOException) {
                // Handle network errors or other exceptions
                e.printStackTrace()
            }
        }
    }
}