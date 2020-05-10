package com.rps.todoreminder.broadcastReciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class ToDoBroadCastReciever : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_LONG).show()
        Log.d("ToDoBroadCastReciever","BroadCastFired------")
    }
}