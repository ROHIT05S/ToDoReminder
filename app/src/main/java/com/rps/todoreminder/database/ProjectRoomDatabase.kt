package com.rps.todoreminder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rps.todoreminder.dao.ToDoDao
import com.rps.todoreminder.entity.ToDoEntity

@Database(entities = [ToDoEntity::class], version = 1)
abstract class ProjectRoomDatabase : RoomDatabase() {
    abstract fun todoDao(): ToDoDao
    companion object {
        @Volatile
        private var INSTANCE: ProjectRoomDatabase? = null

        fun getDatabase(context: Context
        ): ProjectRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProjectRoomDatabase::class.java,
                    "project_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}