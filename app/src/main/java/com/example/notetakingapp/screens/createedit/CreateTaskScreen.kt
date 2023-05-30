package com.example.notetakingapp.screens.createedit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notetakingapp.room.domain.Task
import com.example.notetakingapp.navigation.Routes
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditTaskScreen(
    navController: NavController,
    viewModel: EditTaskViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val context = LocalContext.current

    val priority = Task.Priority.values()
    val states = Task.Status.values()
    var expanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }


//        OPTION 1
//        TopAppBar(
//            title = { Text(text = "Create") },
//            actions = {
//                IconButton(onClick = { /*TODO*/ }) {
//                    Icon(imageVector = Icons.Rounded.Add, contentDescription = "add stuff")
//                }
//            }
//        )

//        OPTION 2
    Scaffold(topBar = {
        TopAppBar {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "back")
            }
            Text(text = if (viewModel.task == null) "Create" else "Update")
        }

    }) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(paddingValues)) {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = viewModel.title,
                    onValueChange = { str -> viewModel.updateTitle(str) },
                    label = { Text(text = "Title") }
                )
                OutlinedTextField(modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 200.dp),
                    value = viewModel.description,
                    onValueChange = { str -> viewModel.updateDesc(str) },
                    label = { Text(text = "Description") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    ExposedDropdownMenuBox(
                        modifier = if (viewModel.task == null) Modifier.fillMaxWidth() else Modifier.weight(
                            0.5f
                        ),
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(value = viewModel.priority.name,
                            label = { Text(text = "Priority") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            onValueChange = {},
                            trailingIcon = {
                                Icon(
                                    imageVector = if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = "Priority Dropdown"
                                )
                            }
                        )
                        ExposedDropdownMenu(expanded = expanded,
                            onDismissRequest = { expanded = false }) {
                            priority.forEach { selectedOption ->
                                DropdownMenuItem(onClick = {
                                    viewModel.updatePriority(priority = selectedOption)
                                    expanded = false
                                }) {
                                    Text(text = selectedOption.toString())
                                }
                            }
                        }
                    }

                    if (viewModel.task != null)
                        Spacer(modifier = Modifier.width(16.dp))

                    if (viewModel.task != null)
                        ExposedDropdownMenuBox(
                            modifier = Modifier.weight(0.5f),
                            expanded = statusExpanded,
                            onExpandedChange = { statusExpanded = !statusExpanded }
                        ) {
                            OutlinedTextField(
                                value = viewModel.status.name,
                                label = { Text(text = "State") },
                                readOnly = true,
                                onValueChange = {},
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (statusExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                        contentDescription = "State Dropdown"
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                            ExposedDropdownMenu(expanded = statusExpanded,
                                onDismissRequest = { statusExpanded = false }) {
                                states.forEach { selectedOption ->
                                    DropdownMenuItem(onClick = {
                                        viewModel.updateStatus(state = selectedOption)
                                        statusExpanded = false
                                    }) {
                                        Text(text = selectedOption.toString())
                                    }
                                }
                            }
                        }
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = SimpleDateFormat(
                        "EEE dd MM yyyy",
                        Locale.getDefault()
                    ).format(viewModel.date.time),
                    onValueChange = {},
                    label = { Text(text = "Date") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            openDatePickerDialog(context = context, calendar = viewModel.date) {
                                viewModel.updateDate(date = it)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.DateRange,
                                contentDescription = "date dropdown"
                            )
                        }

                    }
                )
                OutlinedTextField(
                    value = SimpleDateFormat(
                        "hh:mm a",
                        Locale.getDefault()
                    ).format(viewModel.date.time),
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {},
                    label = { Text(text = "Time") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            openTimePickerDialog(context = context, calendar = viewModel.date) {
                                viewModel.updateTime(time = it)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Timer,
                                contentDescription = "time dropdown"
                            )
                        }

                    }
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    onClick = {
                        viewModel.saveTask {

                            if (viewModel.task == null)
                                navController.popBackStack()
                            else
                                navController.navigate(
                                    Routes.ViewTask.route.plus("?id=").plus(viewModel.task?.id)
                                ) {
                                    popUpTo(Routes.TasksRoute.route) {
                                        inclusive = false
                                    }
                                }
                        }
                    }) { Text(modifier = Modifier.padding(4.dp), text = if (viewModel.task == null) "Create" else "Update") }

            }
        }
    }
}


private fun openDatePickerDialog(context: Context, calendar: Calendar, block: (Calendar) -> Unit) {

    val today = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val dialog = DatePickerDialog(context, { _, mYear, mMonth, mday ->

        val selected = Calendar.getInstance(Locale.getDefault())

        selected.set(Calendar.YEAR, mYear)
        selected.set(Calendar.MONTH, mMonth)
        selected.set(Calendar.DAY_OF_MONTH, mday)

        block.invoke(selected)

    }, year, month, day)

    dialog.datePicker.minDate = today.timeInMillis
    dialog.show()
}

private fun openTimePickerDialog(context: Context, calendar: Calendar, block: (Calendar) -> Unit) {

    val now = Calendar.getInstance()
    now.time = calendar.time

    val hours = now.get(Calendar.HOUR_OF_DAY)
    val minutes = now.get(Calendar.MINUTE)

    val mTimePickerDialog = TimePickerDialog(context, { _, hrs, mins ->

        now.set(Calendar.HOUR_OF_DAY, hrs)
        now.set(Calendar.MINUTE, mins)

        block.invoke(now)

    }, hours, minutes, true)
    mTimePickerDialog.show()
}

@Preview
@Composable
fun CreateTaskPreview() {
    val context = LocalContext.current
    EditTaskScreen(navController = NavController(context))
}