package com.rps.todoreminder

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rps.todoreminder.bindingactionlisteners.ToDetailActivityListener
import com.rps.todoreminder.databinding.ActivityToDoDetailBinding
import com.rps.todoreminder.databinding.TodoDialogBinding
import com.rps.todoreminder.entity.ToDoEntity
import com.rps.todoreminder.viewmodels.MainActivityViewModel
import com.rps.todoreminder.viewmodels.ToDoDetailViewModel

class ToDoDetailActivity : AppCompatActivity(), ToDetailActivityListener {

    lateinit var activityToDoDetailBinding: ActivityToDoDetailBinding
    lateinit var activitiyToDoDetailViewModel: ToDoDetailViewModel
    lateinit var toDoDetail: ToDoEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toDoId = intent.getLongExtra("toDoId", 0)

        activityToDoDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_to_do_detail)
        activitiyToDoDetailViewModel = ViewModelProvider(this).get(ToDoDetailViewModel::class.java)
        activitiyToDoDetailViewModel.getToDoDetail(toDoId).observe(this, Observer { toDoEntity ->
            activityToDoDetailBinding.todoEntity = toDoEntity
            toDoDetail = toDoEntity
        })
        activityToDoDetailBinding.toDoDetailEventListener = this

    }

    override fun onUpdateToDoClick() {
        showToDoAlertDialog()
    }

    fun showToDoAlertDialog() {
        val toDoDialogBinding = DataBindingUtil.inflate<TodoDialogBinding>(
            LayoutInflater.from(this),
            R.layout.todo_dialog,
            null,
            false
        )
        val mBuilder = this?.let {
            AlertDialog.Builder(it)
                .setView(toDoDialogBinding.root)
                .setCancelable(false)
        }
        val alertDialog = mBuilder?.create()
        alertDialog?.show()
        toDoDialogBinding.edTextTodoHeading.setText(toDoDetail.title)
        toDoDialogBinding.crossIcon.setOnClickListener(View.OnClickListener { alertDialog?.hide() })
        toDoDialogBinding.btnSubmit.setOnClickListener({
            var toDoheading = toDoDialogBinding.edTextTodoHeading.text
            var toDoDescription = toDoDialogBinding.edTextTodoDescription.text
            var isRemindMe = toDoDialogBinding.remindMeCheckbox.isChecked
            if (!TextUtils.isEmpty(toDoheading)) {
                updateToDo(toDoheading, toDoDescription, isRemindMe)
                alertDialog?.hide()
            } else {
                toDoDialogBinding.edTextTodoHeading.setError("Heading should not be empty")
            }

        })

    }

    fun updateToDo(toDoheading: Editable?, toDoDescription: Editable?, remindMe: Boolean) {
        activitiyToDoDetailViewModel.updateToDo(ToDoEntity(toDoDetail.id,toDoheading.toString(),toDoDescription.toString(),remindMe))
    }

    override fun onDeleteToDoClick(toDo: ToDoEntity) {
        activitiyToDoDetailViewModel.deleteToDo(toDo)
    }

    override fun onShareToDoClick() {
        val sharingText = "Hi I want to share this toDo with You \n\n"+toDoDetail.title+"\n"+toDoDetail.description
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, sharingText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)

    }

    override fun onRemindToDoCLick(remindMe: Boolean,toDoId: Long) {
        activitiyToDoDetailViewModel.updateReminder(toDoId,remindMe)
    }
}
