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
import android.text.Editable
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

        val editable = Editable.Factory.getInstance()
        binding.tieCategoryFilter.text = editable.newEditable(if(MainActivity.sqLiteManager?.categoryFilter != null) MainActivity.sqLiteManager?.categoryFilter else "")
        binding.tieMinutesToNotification.text = editable.newEditable(if(MainActivity.sqLiteManager?.notifyDelay != null) MainActivity.sqLiteManager?.notifyDelay.toString() else "")
        binding.cbHideDoneTasks.isChecked = if(MainActivity.sqLiteManager?.isDoneFilter == 0) true else false

        binding.btnClose.setOnClickListener {dismiss()}
        binding.btnSave.setOnClickListener {
            setUpFilters()
            setUpNotification()
            dismiss()}
    }
    private fun validateMinutes(minutes: String): Boolean{
        try{
            val m: Int = minutes.toInt()
            if(m < 0) return false
        }catch (e: Exception){return false}
        return true
    }

    private fun setUpFilters(){
        MainActivity.sqLiteManager?.categoryFilter = if(!binding.tieCategoryFilter.text.toString().isEmpty()) binding.tieCategoryFilter.text.toString() else null
        MainActivity.sqLiteManager?.isDoneFilter = if(binding.cbHideDoneTasks.isChecked) 0 else null
        MainActivity.sqLiteManager?.loadToLocalMemory()
    }

    private fun setUpNotification() {
        val notifyMinutes: String = binding.tieMinutesToNotification.text.toString()
        if (validateMinutes(notifyMinutes)) {
            MainActivity.sqLiteManager?.notifyDelay = notifyMinutes.toInt()
            val notificationHandler = context?.let { NotificationHandler(it) }
            notificationHandler?.createNotificationChannel()
            notificationHandler?.scheduleNotification()
        }
    }

}