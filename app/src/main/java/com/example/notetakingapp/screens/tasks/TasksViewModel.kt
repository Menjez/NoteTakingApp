package com.example.notetakingapp.screens.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.notetakingapp.data.repository.TaskRepository
import com.example.notetakingapp.data.repository.TaskRepositoryImpl
import com.example.notetakingapp.room.ToDoAppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ToDoAppDatabase.getDatabase(application)
    private val dao = database.NoteAppDao()

    private val repository: TaskRepository = TaskRepositoryImpl(dao)

    val tasks = repository.getTasks()

    val searchQuery = MutableStateFlow("")
    private val tasksList = searchQuery.flatMapLatest { repository.searchTasks(it) }
    val myTasksList get() = tasksList

    val filterTask = MutableStateFlow("")

    fun updateQuery(str: String) {
        viewModelScope.launch {
            searchQuery.value = str
        }
    }

}