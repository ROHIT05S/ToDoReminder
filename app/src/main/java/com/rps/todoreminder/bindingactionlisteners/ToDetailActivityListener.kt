package com.rps.todoreminder.bindingactionlisteners

import com.rps.todoreminder.entity.ToDoEntity

interface ToDetailActivityListener {
    fun onUpdateToDoClick()
    fun onDeleteToDoClick(toDo:ToDoEntity)
    fun onShareToDoClick()
    fun onRemindToDoCLick(boolean: Boolean,toDoId:Long)
}