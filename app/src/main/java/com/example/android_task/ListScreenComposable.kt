package com.example.android_task

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ListScreen(
    viewModel: ListScreenViewModel
) {
    val listTasks = viewModel.tasks.observeAsState(emptyList())
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(listTasks.value) { task ->
            Text(text = task.title)
        }
    }
}