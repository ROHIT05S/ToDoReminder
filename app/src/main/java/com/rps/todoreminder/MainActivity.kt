package com.rps.todoreminder

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
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
import androidx.lifecycle.ViewModelProvider
import com.rps.todoreminder.adapter.ToDoListAdapter
import com.rps.todoreminder.bindingactionlisteners.ToDoActivityListener
import com.rps.todoreminder.broadcastReciever.ToDoBroadCastReciever
import com.rps.todoreminder.databinding.ActivityMainBinding
import com.rps.todoreminder.databinding.TodoDialogBinding
import com.rps.todoreminder.entity.ToDoEntity
import com.rps.todoreminder.viewmodels.MainActivityViewModel
import java.util.*

class MainActivity : AppCompatActivity(),ToDoActivityListener {

    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var mainActivityViewModel: MainActivityViewModel
    private val REQUEST_CODE = 100
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        activityMainBinding.toDoListener = this
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.allToDoData.observe(this, Observer { toDoList->
            activityMainBinding.todoList = toDoList
            activityMainBinding.toDoListAdapter = ToDoListAdapter(toDoList,{todoId -> onToDoItemClicked(todoId)})
        })

        setBroadCast()


    }

    private fun setBroadCast() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ToDoBroadCastReciever::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Setting the specific time for the alarm manager to trigger the intent, in this example, the alarm is set to go off at 23:30, update the time according to your need
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 15)
        calendar.set(Calendar.MINUTE, 30)

        // Starts the alarm manager
        alarmManager.setRepeating(
            AlarmManager.RTC,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            pendingIntent
        )
        //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,15000,pendingIntent)

    }

    fun onToDoItemClicked(todoId: Long) {
        val intent = Intent(this,ToDoDetailActivity::class.java)
        intent.putExtra("toDoId",todoId)
        startActivity(intent)
    }

    fun showToDoAlertDialog() {
        val toDoDialogBinding = DataBindingUtil.inflate<TodoDialogBinding>(
            LayoutInflater.from(this),
            R.layout.todo_dialog,
            null,
            false
        )
        val dialog = this?.let { Dialog(it) }
        val mBuilder = this?.let {
            AlertDialog.Builder(it)
                .setView(toDoDialogBinding.root)
                .setCancelable(false)
        }
        val alertDialog = mBuilder?.create()
        alertDialog?.show()
        toDoDialogBinding.crossIcon.setOnClickListener(View.OnClickListener { alertDialog?.hide() })
        toDoDialogBinding.btnSubmit.setOnClickListener({
            var toDoheading = toDoDialogBinding.edTextTodoHeading.text
            var toDoDescription = toDoDialogBinding.edTextTodoDescription.text
            var isRemindMe = toDoDialogBinding.remindMeCheckbox.isChecked
            if (!TextUtils.isEmpty(toDoheading)){
                saveDataToDatabase(toDoheading, toDoDescription, isRemindMe)
                alertDialog?.hide()
            }else{
                toDoDialogBinding.edTextTodoHeading.setError("Heading should not be empty")
            }

        })

    }

    fun saveDataToDatabase(
        toDoheading: Editable?,
        toDoDescription: Editable?,
        remindMe: Boolean
    ) {
        mainActivityViewModel.insert(
            ToDoEntity(
                System.currentTimeMillis(),
                toDoheading.toString(),
                toDoDescription.toString(),
                remindMe
            )
        )
        Toast.makeText(this,"Your Data Is Saved",Toast.LENGTH_SHORT).show()
    }

    override fun onCreateToDoClick() {
        showToDoAlertDialog()
    }
    override fun onDestroy() {
        super.onDestroy()
        // Cancels the pendingIntent if it is no longer needed after this activity is destroyed.
        alarmManager.cancel(pendingIntent)
    }

}
