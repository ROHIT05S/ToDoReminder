package com.rps.todoreminder.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rps.todoreminder.R
import com.rps.todoreminder.entity.ToDoEntity

class ToDoListAdapter(val toDoEntityList: List<ToDoEntity>, val todoItemclickListener: (Long) -> Unit) :
    RecyclerView.Adapter<ToDoListAdapter.ToDoItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ToDoItemViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {

        val data: String = toDoEntityList.get(position).title
        val toDoId: Long = toDoEntityList.get(position).id
        holder.bind(data,toDoId,todoItemclickListener)
    }

    override fun getItemCount(): Int = toDoEntityList.size

    class ToDoItemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
       RecyclerView.ViewHolder(inflater.inflate(R.layout.todo_item, parent, false)) {
        private var mTitleView: TextView? = null
        init {
            mTitleView = itemView.findViewById(R.id.todo_item_tv)
        }

        fun bind(data: String, todoId:Long, todoItemclickListener: (Long) -> Unit) {
            mTitleView?.text = data
            itemView.setOnClickListener{(todoItemclickListener(todoId))}
        }
    }
}