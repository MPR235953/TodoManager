package com.example.todomanager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.Date

class NotificationHandler(val context: Context) {

    @SuppressLint("ServiceCast")
    fun createNotification(taskItem: TaskItem) {
        Log.i("INFO","entered schedule")
        val intentToNotification = Intent(context, NotificationReceiver::class.java)
        intentToNotification.putExtra("taskItem", taskItem)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskItem.id.toInt(),
            intentToNotification,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime(taskItem)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        Log.i("INFO","exit schedule")
    }

    fun deleteNotification(taskItem: TaskItem){
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskItem.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    private fun getTime(taskItem: TaskItem): Long{
        val localDateTime = taskItem.dueDateTime?.minusMinutes(MainActivity.sqLiteManager?.notifyDelay!!.toLong())
        if(localDateTime != null) return DataTimeConverter.toMilis(localDateTime)
        else return -1
    }

    fun createNotificationChannel() {
        Log.i("INFO","entered create channel")
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NotificationReceiver.channelID, name, importance)
        channel.description = desc
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        Log.i("INFO","exit create channel")
    }
}