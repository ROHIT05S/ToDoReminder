package com.rps.todoreminder.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rps.todoreminder.database.ProjectRoomDatabase
import com.rps.todoreminder.entity.ToDoEntity
import com.rps.todoreminder.repositories.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ToDoDetailViewModel(application: Application) : AndroidViewModel(application) {
    val toDorepository: ToDoRepository
    init {
        val todoDao = ProjectRoomDatabase.getDatabase(application).todoDao()
        toDorepository = ToDoRepository(todoDao)
    }
    fun getToDoDetail(todoId: Long): LiveData<ToDoEntity> {
        return toDorepository.getToDoDetail(todoId)
    }

    fun updateToDo(todo: ToDoEntity) = viewModelScope.launch(Dispatchers.IO) {
        toDorepository.updateToDO(todo)
    }

    fun updateReminder(todoId: Long,remindMe:Boolean) = viewModelScope.launch(Dispatchers.IO) {
        toDorepository.updateReminder(todoId,remindMe)
    }

    fun deleteToDo(toDo: ToDoEntity) = viewModelScope.launch(Dispatchers.IO) {
        toDorepository.deleteToDo(toDo)
    }

}