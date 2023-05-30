package com.example.notetakingapp.screens.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.notetakingapp.room.domain.Task
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalUnitApi::class)
@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Card(
            border = BorderStroke(0.5.dp, Color.Gray),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .clickable { onClick.invoke() }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = task.title.split(" ").map { it.replaceFirstChar { it.uppercase() } }
                        .joinToString(" "),
                    fontSize = TextUnit(24f, TextUnitType.Sp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(
                            text = SimpleDateFormat(
                                "EEE dd MMM",
                                Locale.getDefault()
                            ).format(task.date.time).plus("  •  ")
                        )

                        Text(
                            text = SimpleDateFormat(
                                "hh:mm a",
                                Locale.getDefault()
                            ).format(task.date.time)
                        )
                    }
                    Text(
                        text = task.priority.toString(),
                        color = when (task.priority) {

                            Task.Priority.HIGH -> Color.Red
                            else-> MaterialTheme.colors.primary
                        }
                    )
                }
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    TaskCard(task = Task(title = "None", description = "Description", date = Date())) {}
}


