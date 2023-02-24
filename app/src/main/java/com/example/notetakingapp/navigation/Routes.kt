package com.example.notetakingapp.navigation

sealed class Routes(val route: String) {
    object TasksRoute : Routes("tasks")
    object Edit : Routes("edit")
    object ViewTask : Routes("task")
}
