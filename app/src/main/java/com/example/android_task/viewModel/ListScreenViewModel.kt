package com.example.android_task.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.android_task.data.entity.SelectTask
import com.example.android_task.data.repos.TasksRepo
import com.example.android_task.work.GetDataWorker
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        viewModelScope.launch {
        val uploadWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<GetDataWorker>(
                1,
                TimeUnit.HOURS
            )
                .build()

        workManager.enqueue(uploadWorkRequest)
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
                    list
                }else {
                    list.filter{
                        item ->
                        item.title.equals(query, ignoreCase = true) ||
                                item.task.equals(query, true) ||
                                item.description.equals(query, true) ||
                                item.wageType.equals(query, true) ||
                                item.sort.toString().equals(query, true) ||
                                item.wageType.equals(query, true) ||
                                item.businessUnitKey?.equals(query, true) == true ||
                                item.businessUnit.equals(query, true) ||
                                item.parentTaskID.equals(query, true) ||
                                item.preplanningBoardQuickSelect?.equals(query, true) == true ||
                                item.colorCode.equals(query, true) ||
                                item.workingTime?.equals(query, true) == true ||
                                item.externalId.toString().equals(query, true) ||
                                item.isAvailableInTimeTrackingKioskMode && query.equals("is Available", true) ||
                                item.isAbstract && query.equals("is Abstract", true)
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun onRefresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            taskRepo.refreshTasks()
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
            } else {
                Log.e("MainActivity", "Scan failed")
            }
        }
    }

}