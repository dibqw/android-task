package com.example.android_task

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_task.data.entity.LoginRequest
import com.example.android_task.data.entity.SelectTask
import com.example.android_task.data.services.ApiServiceImpl
import com.example.android_task.utils.AuthTokenProvider
import com.example.android_task.utils.RetrofitInstance
import kotlinx.coroutines.launch

class ListScreenViewModel: ViewModel() {
    private val _tasks = MutableLiveData<List<SelectTask>>()
    val tasks: LiveData<List<SelectTask>> = _tasks

    init {
        viewModelScope.launch {
            try {
                // Perform initial login explicitly to get the *first* token
                val loginRequest = LoginRequest("365", "1")
                val response = RetrofitInstance.apiService.login(loginRequest)

                if (response.isSuccessful) {
                    val token = response.body()?.oauth?.accessToken
                    AuthTokenProvider.accessToken = token
                    fetchTasks() // Call authorized endpoint
                }
            } catch (e: Exception) {
                // Handle initial login failure
            }
        }
    }

//    fun initializeSessionAndFetchData() {
//        // You still need an initial call to get the *first* token,
//        // unless you store the token in SharedPreferences and load it on app start.
//
//        // If AuthTokenProvider.accessToken is null initially, the first authorized call
//        // (like getTasks below) will trigger the Authenticator immediately.
//        // It's cleaner to log in explicitly first.
//
//        viewModelScope.launch {
//            try {
//                // Perform initial login explicitly to get the *first* token
//                val loginRequest = LoginRequest("365", "1")
//                val response = RetrofitInstance.apiService.login(loginRequest)
//
//                if (response.isSuccessful) {
//                    val token = response.body()?.oauth?.accessToken
//                    AuthTokenProvider.accessToken = token
//                    fetchTasks() // Call authorized endpoint
//                }
//            } catch (e: Exception) {
//                // Handle initial login failure
//            }
//        }
//    }

    fun fetchTasks() {
        viewModelScope.launch {
            ApiServiceImpl.getAllTasks { tasks ->
                _tasks.value = tasks
            }
        }
    }
}