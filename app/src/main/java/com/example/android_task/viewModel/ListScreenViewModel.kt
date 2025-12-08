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
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.android_task.data.entity.SelectTask
import com.example.android_task.data.repos.TasksRepo
import com.example.android_task.utils.AppDatabase
import com.example.android_task.work.GetDataWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ListScreenViewModel @Inject constructor(
    private val taskRepo: TasksRepo,
    private val workManager: WorkManager
) : ViewModel() {

//    private val _tasks = MutableLiveData<List<SelectTask>>()
//    val tasks: LiveData<List<SelectTask>> = _tasks


    init {
        viewModelScope.launch {
        val uploadWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<GetDataWorker>(
                1,
                TimeUnit.HOURS
            )
//                    .setInputData()
                .build()

        workManager.enqueue(uploadWorkRequest)

//            taskRepo.refreshTasks()
//            _tasks.value = taskRepo.getTasks()
        }
    }

    fun fetchTasks() {
        viewModelScope.launch {
            taskRepo.refreshTasks()
//            _tasks.value = taskRepo.getTasks()
        }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }

    @OptIn(FlowPreview::class)
    val listToDisplay: Flow<List<SelectTask>> =
        searchQuery
            .debounce(300L)
            .distinctUntilChanged()
            .combine(taskRepo.getTasks()) {query, list ->
                if (query.isBlank()) {
                    Log.e("ListScreenViewModel1", "Query: $query")
                    list
                }else {
                    Log.e("ListScreenViewModel2", "Query: $query")
                    list.filter{
                        item -> item.title.contains(query, ignoreCase = true)
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

//    fun search(query: String) {
//        viewModelScope.launch {
//            CoroutineScope(Dispatchers.IO).launch {
//                val searchQuery = "%$query%"
//                _tasks.postValue(taskRepo.getTasksByQuery(searchQuery))
//            }
//        }
//    }

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
//                search(contents)
            } else {
                Log.e("MainActivity", "Scan failed")
            }
        }
    }

}