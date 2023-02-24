package com.example.notetakingapp.room

import androidx.annotation.RequiresPermission.Read
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notetakingapp.data.domain.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM NoteAppTable ORDER BY id ASC")
    fun getTaskList():LiveData<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(Task: TaskEntity)

    @Update
    suspend fun update(Task: TaskEntity)

    @Query("SELECT * FROM NoteAppTable where id = :id")
    suspend fun get(id: Int) : TaskEntity


    @Query("DELETE FROM NoteAppTable where id = :id")
    suspend fun delete(id: Int)

   @Query("SELECT * FROM NoteAppTable WHERE Task LIKE '%' || :query || '%'")
   fun searchTasks(query: String) : Flow<List<TaskEntity>>

   @Query("SELECT * FROM NoteAppTable WHERE Status IN :status")
   fun filterTasks(status: String) : Flow<List<TaskEntity>>
}