package com.example.android_task.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import com.example.android_task.data.entity.LoginRequest
import com.example.android_task.data.entity.SelectTask
import com.example.android_task.data.services.ApiServiceImpl
import com.example.android_task.utils.AppDatabase
import com.example.android_task.utils.AuthTokenProvider
import com.example.android_task.utils.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListScreenViewModel(
    private val db: AppDatabase
): ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val applicationContext = checkNotNull(extras[APPLICATION_KEY])
                return ListScreenViewModel(
                    db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "tasks-database"
                    ).build()
                ) as T
            }
        }
    }
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

    fun fetchTasks() {
        viewModelScope.launch {
            ApiServiceImpl.getAllTasks { tasks ->
                _tasks.value = tasks
                CoroutineScope(Dispatchers.IO).launch {
                    db.taskDao().insertAll(tasks)
                }
            }
        }
    }

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun search(query: String) {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val searchQuery = "%$query%"
                _tasks.postValue(db.taskDao().findByQuery(searchQuery))
            }
        }
    }
}