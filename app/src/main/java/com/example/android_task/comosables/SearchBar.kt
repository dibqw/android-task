package com.example.android_task.comosables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_task.ui.theme.AndroidtaskTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    closeSearchBar: () -> Unit
) {
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        inputField = {
            SearchBarDefaults.InputField(
                query =
                    query,
                onQueryChange = {
                    onQueryChange(it)
                },
                onSearch = {
                    onSearch(it)
                },
                expanded = false,
                onExpandedChange = { },
                placeholder = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon =
                    {
                        IconButton(
                            onClick = {
                                closeSearchBar()
                            }
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    },
            )
        },
        expanded = false,
        onExpandedChange = { },
    ) {}
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidtaskTheme {
        MySearchBar(
            "query",
            { },
            { },
            { }
        )
    }
}