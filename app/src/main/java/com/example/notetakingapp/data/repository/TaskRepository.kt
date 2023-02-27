package com.example.notetakingapp.data.repository

import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.example.notetakingapp.data.domain.Task
import com.example.notetakingapp.room.TaskDao
import com.example.notetakingapp.room.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun createTask(task: Task)

    suspend fun updateTask(task: Task): Task

    suspend fun getTask(id: Int): Task

    fun getTasks(): Flow<List<Task>>

    suspend fun deleteTask(id: Int): Boolean

    fun searchTasks(query: String): Flow<List<TaskEntity>>

    fun filterTasks(query: String, filter: String): Flow<List<TaskEntity>>

}

class TaskRepositoryImpl(private val dao: TaskDao) : TaskRepository {

    override suspend fun createTask(task: Task) {
        val entity = task.toEntity()
        dao.insert(Task = entity)
    }

    override suspend fun updateTask(task: Task): Task {
        val entity = task.toEntity()
        dao.update(Task = entity)
        return task

    }

    override suspend fun getTask(id: Int): Task {
        return dao.get(id = id).toDomain()
    }

    override fun getTasks(): Flow<List<Task>> {
        return dao
            .getTaskList()
            .map { entityList ->
                entityList.map { entity ->
                    entity.toDomain()
                }
            }.asFlow()
    }

    override suspend fun deleteTask(id: Int): Boolean {
        dao.delete(id = id)
        return true
    }

    override fun searchTasks(query: String): Flow<List<TaskEntity>> = dao.searchTasks(query)

    override fun filterTasks(query: String, filter: String): Flow<List<TaskEntity>> =
        dao.filterTasks(query, filter.uppercase())

}