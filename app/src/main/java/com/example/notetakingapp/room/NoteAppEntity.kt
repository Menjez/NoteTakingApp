package com.example.notetakingapp.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notetakingapp.data.domain.Task
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "NoteAppTable")
@Parcelize
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "Task") val task: String,
    @ColumnInfo(name = "Description") val desc: String,
    @ColumnInfo(name = "Priority") val priority: String = Task.Priority.LOW.name,
    @ColumnInfo(name = "Status") val status: String = Task.Status.PENDING.name,
    @ColumnInfo(name = "Date") val date: Long,
    @ColumnInfo(name = "Time") val time: Int,
) : Parcelable {

    fun toDomain() = Task(
        id = id,
        title = task,
        description = desc,
        priority = Task.Priority.valueOf(priority),
        status = Task.Status.valueOf(status),
        date = Date().apply { time = this@TaskEntity.date }
    )

}

