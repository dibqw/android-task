package com.example.android_task.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import com.example.android_task.data.entity.SelectTask
import com.example.android_task.data.repos.TasksRepo
import com.example.android_task.utils.AppDatabase
import com.example.android_task.utils.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListScreenViewModel(
    private val taskRepo: TasksRepo
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val applicationContext = checkNotNull(extras[APPLICATION_KEY])
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "tasks-database"
                ).build()
                return ListScreenViewModel(
                    taskRepo = TasksRepo(
                        taskDao = db.taskDao(),
                        apiService = RetrofitInstance.apiService
                    )
                ) as T
            }
        }
    }

    private val _tasks = MutableLiveData<List<SelectTask>>()
    val tasks: LiveData<List<SelectTask>> = _tasks


    init {
        viewModelScope.launch {
            taskRepo.refreshTasks()
            _tasks.value = taskRepo.getTasks()
        }
    }

    fun fetchTasks() {
        viewModelScope.launch {
            taskRepo.refreshTasks()
            _tasks.value = taskRepo.getTasks()
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
                _tasks.postValue(taskRepo.getTasksByQuery(searchQuery))
            }
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun onRefresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            fetchTasks()
            _isRefreshing.value = false
        }
    }

    private val _searchBarVisible = MutableStateFlow(false)
    val searchBarVisible: StateFlow<Boolean> = _searchBarVisible

    fun toggleSearchBarVisibility() {
        _searchBarVisible.value = !_searchBarVisible.value
    }

    fun handleScanResult(contents: String?) {
        viewModelScope.launch {
            if (contents != null) {
                Log.e("Scanner", "Scanned: $contents")
                _searchQuery.value = contents
                _searchBarVisible.value = true
                search(contents)
            } else {
                Log.e("MainActivity", "Scan failed")
            }
        }
    }

}