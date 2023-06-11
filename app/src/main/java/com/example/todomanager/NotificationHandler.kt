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
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra("taskId", taskItem.id.toString())
        intent.putExtra("taskName", taskItem.name)
        intent.putExtra("taskDescription", taskItem.description)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskItem.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime(taskItem)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, "sth", "sth")
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

    fun updateNotification(){

    }

    private fun showAlert(time: Long, title: String, message: String)
    {
        Log.i("INFO","entered alert")
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(context)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(context)

        AlertDialog.Builder(context)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("Okay"){_,_ ->}
            .show()
        Log.i("INFO","exit alert")
    }

    private fun getTime(taskItem: TaskItem): Long{
        val localDateTime = taskItem.dueDateTime?.minusMinutes(MainActivity.sqLiteManager?.notifyDelay!!.toLong())
        if(localDateTime != null) return DataTimeConverter.toMilis(localDateTime)
        else return -1

        /*Log.i("INFO","entered time")
        val calendar = Calendar.getInstance()
        val cdt = LocalDateTime.now().plusMinutes(minutes.toLong())
        Log.i("INFO","${cdt.year}, ${cdt.monthValue-1}, ${cdt.dayOfMonth}, ${cdt.hour}, ${cdt.minute}, ${cdt.second}")
        calendar.set(cdt.year, cdt.monthValue-1, cdt.dayOfMonth, cdt.hour, cdt.minute, cdt.second)
        Log.i("INFO","exit time")
        return calendar.timeInMillis*/

    }

    fun createNotificationChannel()
    {
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