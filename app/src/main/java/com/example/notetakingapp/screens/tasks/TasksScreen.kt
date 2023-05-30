package com.example.notetakingapp.screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notetakingapp.navigation.Routes
import com.example.notetakingapp.screens.composables.TaskCard

@Composable
fun TasksScreen(
    navController: NavController,
    viewModel: TasksViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val query by viewModel.searchQuery.collectAsState()
    val list by viewModel.myTasksList.collectAsState(initial = listOf())
    val tabTitles = listOf("All", "Pending", "Started", "Done")
    var tabIndex by remember { mutableStateOf(0) }

    var isSearching by remember { mutableStateOf(false) }



    Scaffold(
        topBar = {
            if (isSearching.not())
                TopAppBar(
                    elevation = 0.dp,
                    title = { Text(text = "Tasks") },
                    actions = {
                        IconButton(
                            modifier = Modifier.padding(4.dp),
                            onClick = { isSearching = isSearching.not() }) {
                            Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search")
                        }
                        IconButton(modifier = Modifier.padding(4.dp), onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Rounded.List, contentDescription = "Filter")
                        }
                    }
                )
            else
                TopAppBar {
                    IconButton(onClick = { isSearching = isSearching.not() }) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = "Stop Search")
                    }
                    TextField(
                        value = query,
                        onValueChange = {
                            viewModel.updateQuery(it)
                        },
                        trailingIcon = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(imageVector = Icons.Rounded.Search, contentDescription = "")
                            }
                        })
                }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.Edit.route) }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add task")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(color = Color(0xFFFAFAFA))
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(selectedTabIndex = tabIndex) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = tabIndex == index,
                            onClick = {
                                tabIndex = index
                                viewModel.updateFilter(title)
                            },
                            text = { Text(text = title) }
                        )

                    }

                }

            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(it)
            ) {
                items(items = list) { task ->         //listOf
                    TaskCard(task.toDomain()) {
                        navController.navigate(
                            Routes.ViewTask.route.plus("?id=").plus(task.id.toString())
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ListScreenPreview() {
    val context = LocalContext.current
    TasksScreen(navController = NavController(context))
}