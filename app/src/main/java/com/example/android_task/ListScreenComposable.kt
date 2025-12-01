package com.example.android_task

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_task.ui.theme.AndroidtaskTheme
import com.example.android_task.utils.convertToColor

@Composable
fun ListScreen(
    viewModel: ListScreenViewModel
) {
    val listTasks = viewModel.tasks.observeAsState(emptyList())
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(listTasks.value) { task ->
            ItemCard(
                task.task,
                task.title,
                task.description,
                task.colorCode.convertToColor()
            )
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
        Column(Modifier.fillMaxSize()) {
            ItemCard(
                task = "task",
                title = "Title",
                description = "description rbetb rvetbvet ervetge erverv rvrv brbrtbrtr  eee5gb egegee5g ree5ge5ge5g eee5e5ge5",
                color = Color.Blue
            )
        }
    }
}