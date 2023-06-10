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
        const val notificationID = 1
        const val channelID = "channel1"
        const val titleExtra = "titleExtra"
        const val messageExtra = "messageExtra"
    }

    override fun onReceive(context: Context, intent: Intent)
    {
        Log.i("INFO","entered receiver")
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(
                context,
                notificationID,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            ))
            .build()

        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
        Log.i("INFO","exit receiver")
    }
}