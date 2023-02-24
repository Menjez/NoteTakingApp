package com.example.notetakingapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class ToDoAppDatabase : RoomDatabase() {
    abstract fun NoteAppDao():TaskDao

    companion object{
        @Volatile
        private var INSTANCE: ToDoAppDatabase ? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context):ToDoAppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, ToDoAppDatabase::class.java,"task_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}