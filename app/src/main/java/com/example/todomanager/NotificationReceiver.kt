package com.example.todomanager

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {
    companion object{
        const val channelID = "channel1"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val taskItem: TaskItem = intent.getSerializableExtra("taskItem") as TaskItem
        Log.i("INFO","entered receiver")
        val intentToMain = Intent(context, MainActivity::class.java)
        intentToMain.putExtra("taskItem", taskItem)
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(taskItem.name)
            .setContentText(taskItem.description)
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(
                context,
                taskItem.id.toInt(),
                intentToMain,
                PendingIntent.FLAG_UPDATE_CURRENT
            ))
            .build()

        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(taskItem.id.toInt(), notification)
        Log.i("INFO","exit receiver")
    }
}