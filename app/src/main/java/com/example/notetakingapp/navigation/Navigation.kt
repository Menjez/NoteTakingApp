package com.example.notetakingapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notetakingapp.screens.createedit.EditTaskScreen
import com.example.notetakingapp.screens.task.ViewTaskScreen
import com.example.notetakingapp.screens.tasks.TasksScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.TasksRoute.route
    ) {
        composable(route = Routes.TasksRoute.route) {
            TasksScreen(navController = navController)
        }

        composable(
            route = Routes.Edit.route.plus("?id={id}"),
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    nullable = true
                }
            )) {
            EditTaskScreen(navController = navController)
        }
        composable(
            route = Routes.ViewTask.route.plus("?id={id}"),
            arguments = listOf(
                navArgument("id"){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            ViewTaskScreen(navController = navController)
        }
    }
}
