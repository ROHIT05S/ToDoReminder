package com.rps.todoreminder.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Query
import com.rps.todoreminder.dao.ToDoDao
import com.rps.todoreminder.entity.ToDoEntity

class ToDoRepository(private val todoDao: ToDoDao) {
    val todos: LiveData<List<ToDoEntity>> = todoDao.getAllTodos()

    @WorkerThread
    suspend fun insert(todo : ToDoEntity){
        todoDao.insert(todo)
    }

    @WorkerThread
    suspend fun updateToDO(todo:ToDoEntity){
        todoDao.updateToDo(todo.id,todo.title,todo.description,todo.remindMe)
    }

    @WorkerThread
    suspend fun updateReminder(todoId:Long,remindMe:Boolean){
        todoDao.updateReminder(todoId,remindMe)
    }

    fun getToDoDetail(todoId: Long): LiveData<ToDoEntity> {
       return todoDao.getToDoDetail(todoId)
    }

    suspend fun deleteToDo(toDo: ToDoEntity){
        return todoDao.deleteToDo(toDo)
    }
}