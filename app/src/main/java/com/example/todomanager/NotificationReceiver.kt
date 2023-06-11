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

    override fun onReceive(context: Context, intent: Intent)
    {
        val taskId: Int? = intent.getStringExtra("taskId")?.toInt()
        val name = intent.getStringExtra("taskName")
        val description = intent.getStringExtra("taskDescription")
        Log.i("INFO","entered receiver")
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(name)
            .setContentText(description)
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(
                context,
                taskId!!,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            ))
            .build()

        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(taskId, notification)
        Log.i("INFO","exit receiver")
    }
}