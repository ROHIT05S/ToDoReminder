package com.rps.todoreminder.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rps.todoreminder.entity.ToDoEntity

@Dao
interface ToDoDao {

    @Query("SELECT * from todo_table")
    fun getAllTodos() : LiveData<List<ToDoEntity>>

    @Query("SELECT * from todo_table WHERE id =:toDoId")
    fun getToDoDetail( toDoId: Long) : LiveData<ToDoEntity>

    @Insert
    suspend fun insert(todo : ToDoEntity)

    @Update
    suspend fun update(todo: ToDoEntity)

    @Query("UPDATE todo_table SET title = :title,description =:description,remindMe=:remindMe  WHERE id = :toDoId")
    suspend fun updateToDo(toDoId:Long,title:String,description:String,remindMe:Boolean)

    @Query("UPDATE todo_table SET remindMe=:remindMe  WHERE id = :toDoId")
    suspend fun updateReminder(toDoId:Long,remindMe:Boolean)

    @Query("DELETE FROM todo_table")
    fun deleteAll()

    @Delete
    suspend fun deleteToDo(todo: ToDoEntity)
}


