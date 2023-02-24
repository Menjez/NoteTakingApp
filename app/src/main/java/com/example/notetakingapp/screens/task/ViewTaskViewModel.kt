package com.example.notetakingapp.screens.task

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.notetakingapp.data.domain.Task
import com.example.notetakingapp.data.repository.TaskRepositoryImpl
import com.example.notetakingapp.room.ToDoAppDatabase
import kotlinx.coroutines.launch

class TaskViewModel(app: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(app) {

    private val taskId = savedStateHandle.get<String>("id")

    private val database = ToDoAppDatabase.getDatabase(context = app)
    private val dao = database.NoteAppDao()
    private val repository = TaskRepositoryImpl(dao = dao)

    var task by mutableStateOf<Task?>(null)
        private set

    init {
        Log.i("TASK","Task id=$taskId")
        getTask()
    }

    private fun getTask() {
        viewModelScope.launch {
            if (taskId != null) {
                task = repository.getTask(id = taskId.toInt())
            }
        }
    }
    fun deleteTask(block: () -> Unit) {
        viewModelScope.launch {
            taskId?.let {
                repository.deleteTask(id = it.toInt())
                block.invoke()
            }
        }
    }

}