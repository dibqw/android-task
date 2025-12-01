package com.example.android_task.data.services

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.android_task.data.entity.SelectTask
import com.example.android_task.utils.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiServiceImpl {
    companion object {
        fun getAllTasks(implementLayout: (List<SelectTask>) -> Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val apiService = RetrofitInstance.apiService
                    val response = apiService.getAllTasks()
                    Log.i("HTTP", "${response.code()}")
                    Handler(Looper.getMainLooper()).post {
                        //Code that runs in main
                        if (response.isSuccessful) {
                            val tasks = response.body()
                            tasks?.forEach {
                                Log.d("MainActivity", "Category: ${it.title}")
                            }
                        } else {
                            Log.e("MainActivity", "Request failed with code: ${response.code()}")
                        }
                        implementLayout(response.body()!!)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}