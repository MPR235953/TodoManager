package com.example.todomanager

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todomanager.databinding.FragmentSettingsSheetBinding
import com.example.todomanager.databinding.FragmentTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.math.min

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
            var m: Int = minutes.toInt()
            if(m < 0) return false
        }catch (e: Exception){return false}
        return true
    }

    private fun enableNotifier(){
        val notifyMinutes: String = binding.tieMinutesToNotification.text.toString()
        if (validateNotifier(notifyMinutes)){
            MainActivity.sqLiteManager?.notifyDelay = notifyMinutes.toInt()
        }
    }

}