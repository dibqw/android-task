package com.example.android_task.comosables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_task.R
import com.example.android_task.data.entity.SelectTask
import com.example.android_task.viewModel.ListScreenViewModel
import com.example.android_task.ui.theme.AndroidtaskTheme
import com.example.android_task.utils.convertToColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListScreenViewModel = viewModel(factory = ListScreenViewModel.Factory)
) {
    var searchBarVisible by rememberSaveable { mutableStateOf(false) }

    val listTasks = viewModel.tasks.observeAsState(emptyList())
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.observeAsState("")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (searchBarVisible) {
                MySearchBar(
                    searchQuery,
                    { viewModel.onQueryChanged(it) },
                    { viewModel.search(it) },
                    {
                        searchBarVisible = false
                        viewModel.onQueryChanged("")
                        viewModel.fetchTasks()
                    }
                )
            } else {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            "App Bar",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { searchBarVisible = !searchBarVisible }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search"
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                painter = painterResource(id = R.drawable.qr_code_24px),
                                contentDescription = "Scan QR Code"
                            )
                        }
                    },
                )
            }
        }
    ) { innerPadding ->
        LazyList(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            listTasks = listTasks.value,
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.onRefresh() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyList(
    modifier: Modifier,
    listTasks: List<SelectTask>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(listTasks) { task ->
                ItemCard(
                    task.task,
                    task.title,
                    task.description,
                    task.colorCode.convertToColor()
                )
            }
        }
    }
}

@Composable
fun ItemCard(
    task: String,
    title: String,
    description: String,
    color: Color?
) {
    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color ?: Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = task,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Title: ",
                    Modifier.padding(end = 5.dp),
                    style = MaterialTheme.typography.titleSmall,
                    )
                Text(text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    )
            }
            Column (
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp),
            ) {
                Text(text = "Description: ",
                    Modifier.padding(end = 5.dp),
                    style = MaterialTheme.typography.titleSmall
                    )
                Text(text = description,
                    Modifier.padding(10.dp),
                    style = MaterialTheme.typography.bodyMedium,)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewList() {
    AndroidtaskTheme {
//        Column(Modifier.fillMaxSize()) {
            LazyList(
                modifier = Modifier.fillMaxSize(),
                listTasks = listOf(
                    SelectTask(
                        1,
                        "task",
                        "title",
                        "description",
                        "")),
                isRefreshing = false,
                onRefresh = {}
            )
//            ItemCard(
//                task = "task",
//                title = "Title",
//                description = "description rbetb rvetbvet ervetge erverv rvrv brbrtbrtr  eee5gb egegee5g ree5ge5ge5g eee5e5ge5",
//                color = Color.Blue
//            )
//        }
    }
}