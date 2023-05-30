package com.example.notetakingapp.screens.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notetakingapp.room.TaskRepository
import com.example.notetakingapp.room.TaskRepositoryImpl
import com.example.notetakingapp.room.ToDoAppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ToDoAppDatabase.getDatabase(application)
    private val dao = database.NoteAppDao()

    private val repository: TaskRepository = TaskRepositoryImpl(dao)

    val searchQuery = MutableStateFlow("")
    private val filterQuery = MutableStateFlow("all")

    private val tasksList = searchQuery.combine(filterQuery) { query, filter ->
      //Log.i("MESSAGE", "$filter ")
        if (filter.equals("all", true))
            repository.searchTasks(query = query).first()
        else
            repository.filterTasks(query, filter).first()
    }
    val myTasksList get() = tasksList


    fun updateQuery(str: String) {
        viewModelScope.launch {
            searchQuery.value = str
        }
    }

    fun updateFilter(title: String) {
        viewModelScope.launch {
            filterQuery.value = title
        }
    }


}