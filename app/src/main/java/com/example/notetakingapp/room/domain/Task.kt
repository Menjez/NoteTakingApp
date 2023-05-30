package com.example.notetakingapp.room.domain

import com.example.notetakingapp.room.TaskEntity
import java.util.Date

data class Task(
    val id: Int = 0,
    var title: String,
    var description: String,
    val date: Date,
    val status: Status = Status.PENDING,
    var priority: Priority = Priority.LOW
) {

    enum class Status {
        PENDING, DONE, STARTED
    }

    enum class Priority {
        LOW, MIDDLE, HIGH
    }

    fun toEntity() = TaskEntity(
        id = id,
        task = title,
        desc = description,
        priority = priority.name,
        status = status.name,
        date = date.time,
        time = 1000
    )

}