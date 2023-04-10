package com.example.notetakingapp.screens.task

import androidx.compose.animation.VectorConverter
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notetakingapp.data.domain.Task
import com.example.notetakingapp.navigation.Routes
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.exp

@OptIn(ExperimentalUnitApi::class, ExperimentalMaterialApi::class)
@Composable
fun ViewTaskScreen(
    navController: NavController,
    viewModel: TaskViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val list = listOf(Pair("Update") {
        navController.navigate(
            Routes.Edit.route.plus("?id=").plus(viewModel.task?.id.toString())
        ) {
            popUpTo(Routes.TasksRoute.route) {
                inclusive = false
            }
        }
    }, Pair("Delete") { viewModel.deleteTask { navController.popBackStack() } }
    )



    Scaffold(topBar = {
        TopAppBar {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Rounded.KeyboardArrowLeft, contentDescription = "Back")
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                UpdateDeleteMenu(list = list, expanded = expanded) {
                    expanded = !expanded
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "Update and Delete"
                    )
                }
            }
        }
    }) {
        Surface(modifier = Modifier.padding(it)) {
            Column(modifier = Modifier.padding(8.dp)) {

                OutlinedTextField(
                    value = viewModel.task?.title ?: "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    label = {
                        Text(
                            text = "Title",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3F)
                        )
                    }
                )
                OutlinedTextField(
                    value = viewModel.task?.description ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 200.dp),
                    readOnly = true,
                    label = {
                        Text(
                            text = "Description",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3F)
                        )
                    }
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Priority:",
                        fontSize = TextUnit(24f, TextUnitType.Sp),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Chip(
                        onClick = { /*TODO*/ },
                        enabled = false,
                        colors = ChipDefaults.chipColors(
                            contentColor = when (viewModel.task?.priority) {
                                Task.Priority.LOW -> Color.Green
                                Task.Priority.MIDDLE -> Color.Yellow
                                Task.Priority.HIGH -> Color.Red
                                null -> Color.Gray
                            },
                            disabledBackgroundColor = Color.LightGray
                        )
                    ) {
                        Text(text = viewModel.task?.priority.toString())
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Chip(onClick = { /*TODO*/ },
                        enabled = false,
                        colors = ChipDefaults.chipColors(disabledBackgroundColor = Color.LightGray),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Check, contentDescription = "State Chip"
                            )
                        }

                    ) {
                        Text(text = viewModel.task?.status.toString(), color = Color.Black)
                    }

                    Chip(onClick = { /*TODO*/ },
                        enabled = false,
                        colors = ChipDefaults.chipColors(disabledBackgroundColor = Color.LightGray),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.DateRange,
                                contentDescription = "Date Chip"
                            )
                        }

                    ) {
                        Text(
                            text = SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault()).format(
                                viewModel.task?.date ?: Date()
                            ), color = Color.Black
                        )
                    }
                    Chip(onClick = { /*TODO*/ },
                        enabled = false,
                        colors = ChipDefaults.chipColors(disabledBackgroundColor = Color.LightGray),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Timer,
                                contentDescription = "Date Chip"
                            )
                        }

                    ) {
                        Text(
                            text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(
                                viewModel.task?.date ?: Date()
                            ), color = Color.Black
                        )
                    }

                }

                Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    ExtendedFloatingActionButton(
                        onClick = { viewModel.deleteTask { navController.popBackStack() } },
                        icon = { Icon(Icons.Filled.Delete, "delete button") },
                        text = { Text(text = "DELETE TASK") }
                    )
                    ExtendedFloatingActionButton(
                        onClick = {
                            navController.navigate(
                                Routes.Edit.route.plus("?id=").plus(viewModel.task?.id.toString())
                            ) {
                                popUpTo(Routes.TasksRoute.route) {
                                    inclusive = false
                                }
                            }
                        },
                        icon = { Icon(Icons.Filled.Edit, "edit button") },
                        text = { Text(text = "EDIT TASK") }
                    )
                }
            }
        }
    }
}

@Composable
fun UpdateDeleteMenu(
    expanded: Boolean, list: List<Pair<String, () -> Unit>>, onDismiss: () -> Unit
) {

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        DropdownMenu(expanded = expanded, onDismissRequest = { onDismiss.invoke() }) {
            list.forEach { (title, block) ->
                DropdownMenuItem(onClick = {
                    block.invoke()
                    onDismiss.invoke()
                }) {
                    Text(text = title)
                }
            }
        }
    }
}

@Preview
@Composable
fun TaskScreenPreview() {
    val context = LocalContext.current
    ViewTaskScreen(navController = NavController(context))
}