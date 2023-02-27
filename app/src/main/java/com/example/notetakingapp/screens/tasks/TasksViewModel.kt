package com.example.notetakingapp.screens.tasks

import android.app.Application
import android.app.StatusBarManager
import android.icu.text.CaseMap.Title
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.notetakingapp.data.domain.Task
import com.example.notetakingapp.data.repository.TaskRepository
import com.example.notetakingapp.data.repository.TaskRepositoryImpl
import com.example.notetakingapp.room.TaskEntity
import com.example.notetakingapp.room.ToDoAppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ToDoAppDatabase.getDatabase(application)
    private val dao = database.NoteAppDao()

    private val repository: TaskRepository = TaskRepositoryImpl(dao)

    val tasks = repository.getTasks()

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