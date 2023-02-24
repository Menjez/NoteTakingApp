package com.example.notetakingapp.screens.ViewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.notetakingapp.data.domain.Task
import com.example.notetakingapp.data.repository.TaskRepository
import com.example.notetakingapp.data.repository.TaskRepositoryImpl
import com.example.notetakingapp.room.ToDoAppDatabase
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.*

class EditTaskViewModel(
    app: Application, savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {

    private val taskId = savedStateHandle.get<String?>("id")

    private val database = ToDoAppDatabase.getDatabase(app)
    private val dao = database.NoteAppDao()

    private val repository: TaskRepository = TaskRepositoryImpl(dao = dao)

    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var priority by mutableStateOf(Task.Priority.LOW)
        private set
    var status by mutableStateOf(Task.Status.PENDING)
        private set

    var date by mutableStateOf(Calendar.getInstance())
        private set

    var task by mutableStateOf<Task?>(null)
        private set

    init {
        getTask()
    }

    private fun updateTask() {
        task?.let {
            viewModelScope.launch {
                val mTask = Task(
                    id = it.id,
                    title = title,
                    description = description,
                    date = date.time,
                    priority = priority,
                    status = status,
                )
                repository.updateTask(task = mTask)
            }
        }
    }

    private fun createTask() {
        viewModelScope.launch {
            repository.createTask(
                task = Task(
                    title = title, description = description, date = date.time
                )
            )
        }
    }

    private fun getTask() {
        if (taskId != null)
            viewModelScope.launch {
                val mTask = dao.get(id = taskId.toInt()).toDomain()
                updateTask(task = mTask)
            }
    }

    private fun updateTask(task: Task) {

        val calendar = Calendar.getInstance()
        calendar.time = task.date

        this.task = task
        title = task.title
        description = task.description
        priority = task.priority
        status = task.status
        date = calendar
    }

    fun updateTitle(str: String) {
        title = str
    }

    fun updateDesc(str: String) {
        description = str
    }

    fun updatePriority(priority: Task.Priority) {
        this.priority = priority
    }

    fun updateStatus(state: Task.Status) {
        this.status = state
    }

    fun updateDate(date: Calendar) {
        this.date = date
    }

    fun updateTime(time: Calendar) {
        this.date = time
    }

    fun saveTask(block: () -> Unit) {
        when (task == null) {
            true -> createTask()
            false -> updateTask()
        }
        block.invoke()
    }



}