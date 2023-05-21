package com.example.todomanager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todomanager.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class NewTaskSheet(context: Context, var taskItem: TaskItem?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private var dueDateTime: LocalDateTime? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        // decide what will be seen when add button was clicked
        if(taskItem != null){
            binding.tvTaskTitle.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            binding.tieName.text = editable.newEditable(taskItem!!.name)
            binding.tieDescription.text = editable.newEditable(taskItem!!.description)
            if(taskItem!!.dueDateTime != null){
                dueDateTime = taskItem!!.dueDateTime!!
                updateDateTimeOnButton()
            }
        }
        else binding.tvTaskTitle.text = "New Task"

        // save new task
        binding.btnSave.setOnClickListener{
            saveAction()
        }
        binding.btnPickDueDateTime.setOnClickListener{
            openDateTimePicker()
        }
    }

    private fun updateDateTimeOnButton() {
        binding.btnPickDueDateTime.text = dueDateTime!!.format(DateTimeFormatter.ofPattern("yy/MM/dd hh:mm"))
    }

    private fun openDateTimePicker(){
        if(dueDateTime == null) dueDateTime = LocalDateTime.now()

        // Time Picker stuff
        val timeListener = TimePickerDialog.OnTimeSetListener{ _, h, m ->
            dueDateTime = dueDateTime!!.toLocalDate().atTime(LocalTime.of(h, m))
        }
        val timeDialog = TimePickerDialog(activity as Context, timeListener, dueDateTime!!.hour, dueDateTime!!.minute, true)
        timeDialog.setTitle("Task Due Time")
        timeDialog.show()

        // Date Picker stuff
        val dateListener = DatePickerDialog.OnDateSetListener{ _, y, m, d ->
            dueDateTime = dueDateTime!!.toLocalTime().atDate(LocalDate.of(y, m, d))
        }
        val dateDialog = DatePickerDialog(activity as Context, dateListener, dueDateTime!!.year, dueDateTime!!.month.value, dueDateTime!!.dayOfMonth)
        dateDialog.setTitle("Task Due Date")
        dateDialog.show()
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveAction(){
        val sqLiteManager: SQLiteManager? = SQLiteManager.instanceOfDatabase(context)

        // get data from layout
        val name = binding.tieName.text.toString()
        val description = binding.tieDescription.text.toString()

        // create new task item or update existing
        if(taskItem == null){
            val newTask = TaskItem(name, description, dueDateTime, null)
            TaskViewModel.addTaskItem(newTask)
            sqLiteManager?.addTaskItem(newTask)
        }
        else{
            TaskViewModel.updateTaskItem(taskItem!!.id, name, description, dueDateTime, null)
        }

        // clear data on view
        binding.tieName.setText("")
        binding.tieDescription.setText("")
        dismiss()
    }

}