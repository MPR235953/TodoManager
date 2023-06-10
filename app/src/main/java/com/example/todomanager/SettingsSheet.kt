package com.example.todomanager

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todomanager.databinding.FragmentSettingsSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.Exception
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import java.util.*
import android.app.*
import android.util.Log
import java.time.LocalDateTime

class SettingsSheet(context: Context) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSettingsSheetBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
            }
        }
        return dialog
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClose.setOnClickListener {dismiss() }
        binding.btnSave.setOnClickListener {
            setUpFilters()
            enableNotifier()
            dismiss()
        }
    }

    private fun setUpFilters(){
        MainActivity.sqLiteManager?.isDoneFilter = if(binding.cbHideDoneTasks.isChecked) 0 else null
        val temp: String = binding.tieCategoryFilter.text.toString()
        if(!temp.isEmpty()) MainActivity.sqLiteManager?.categoryFilter = temp
        else MainActivity.sqLiteManager?.categoryFilter = null
        MainActivity.sqLiteManager?.loadToLocalMemory()
    }

    private fun validateNotifier(minutes: String): Boolean{
        try{
            val m: Long = minutes.toLong()
            if(m < 0) return false
        }catch (e: Exception){return false}
        return true
    }

    private fun enableNotifier(){
        Log.i("INFO","entered enableNotifier")
        createNotificationChannel()
        val notifyMinutes: String = binding.tieMinutesToNotification.text.toString()
        //if (validateNotifier(notifyMinutes)){
        //    MainActivity.sqLiteManager?.notifyDelay = notifyMinutes.toInt()
            scheduleNotification()
        //}
        Log.i("INFO","exit enableNotifier")
    }

    @SuppressLint("ServiceCast")
    private fun scheduleNotification()
    {
        Log.i("INFO","entered schedule")
        val intent = Intent(context, Notification::class.java)
        val title = "UWAGA"
        val message = "noti"
        intent.putExtra(Notification.titleExtra, title)
        intent.putExtra(Notification.messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Notification.notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, title, message)
        Log.i("INFO","exit schedule")
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

    private fun getTime(): Long
    {
        Log.i("INFO","entered time")
        val calendar = Calendar.getInstance()
        val cdt = LocalDateTime.now().plusSeconds(10)
        Log.i("INFO","${cdt.year}, ${cdt.monthValue-1}, ${cdt.dayOfMonth}, ${cdt.hour}, ${cdt.minute}, ${cdt.second}")
        calendar.set(cdt.year, cdt.monthValue-1, cdt.dayOfMonth, cdt.hour, cdt.minute, cdt.second)
        Log.i("INFO","exit time")
        return calendar.timeInMillis
    }

    private fun createNotificationChannel()
    {
        Log.i("INFO","entered create channel")
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Notification.channelID, name, importance)
        channel.description = desc
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        Log.i("INFO","exit create channel")
    }

}