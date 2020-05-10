package com.rps.todoreminder.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rps.todoreminder.database.ProjectRoomDatabase
import com.rps.todoreminder.entity.ToDoEntity
import com.rps.todoreminder.repositories.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    val toDorepository: ToDoRepository
    val allToDoData: LiveData<List<ToDoEntity>> get() = toDorepository.todos
    val toDoClassName: String = "ToDoFragmentViewModel"

    init {
        val todoDao = ProjectRoomDatabase.getDatabase(application).todoDao()
        toDorepository = ToDoRepository(todoDao)
    }

    fun insert(todo: ToDoEntity) = viewModelScope.launch(Dispatchers.IO) {
        toDorepository.insert(todo)
    }

}