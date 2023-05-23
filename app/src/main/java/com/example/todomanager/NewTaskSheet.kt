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

        // decide what will be seen when add button was clicked
        if(taskItem != null){
            // set proper view
            binding.tvTaskTitle.text = "Edit Task"
            binding.btnDelete.visibility = View.VISIBLE
            binding.container.visibility = View.VISIBLE

            val editable = Editable.Factory.getInstance()
            binding.tieName.text = editable.newEditable(taskItem!!.name)
            binding.tieDescription.text = editable.newEditable(taskItem!!.description)
            binding.tieCategory.text = editable.newEditable(taskItem!!.category)
            binding.tvCreatedDateTime.text = DataTimeConverter.dateTime2String(taskItem!!.createDateTime)
            binding.tvDueDateTime.text = DataTimeConverter.dateTime2String(taskItem!!.dueDateTime!!)

            if(taskItem!!.dueDateTime != null){
                dueDateTime = taskItem!!.dueDateTime!!
            }
        }
        else{
            // set proper view
            binding.tvTaskTitle.text = "New Task"
            binding.btnDelete.visibility = View.GONE
            binding.container.visibility = View.GONE
        }

        // btn listeners
        binding.ibtnDateTime.setOnClickListener{
            openDateTimePicker()
        }
        binding.btnSave.setOnClickListener{
            saveAction()
        }
        binding.btnDelete.setOnClickListener{
            deleteAction()
        }
        binding.btnClose.setOnClickListener{
            dismiss()
        }
    }

    private fun showChangedDateTime(){
        binding.tvDueDateTime.text = DataTimeConverter.dateTime2String(this.dueDateTime!!)
    }

    private fun openDateTimePicker(){
        if(this.dueDateTime == null) this.dueDateTime = LocalDateTime.now()

        // Time Picker stuff
        val timeListener = TimePickerDialog.OnTimeSetListener{ _, h, m ->
            this.dueDateTime = this.dueDateTime!!.toLocalDate().atTime(LocalTime.of(h, m))
            showChangedDateTime()
        }
        val timeDialog = TimePickerDialog(activity as Context, timeListener, this.dueDateTime!!.hour, this.dueDateTime!!.minute, true)
        timeDialog.setTitle("Task Due Time")
        timeDialog.show()

        // Date Picker stuff
        val dateListener = DatePickerDialog.OnDateSetListener{ _, y, m, d ->
            this.dueDateTime = this.dueDateTime!!.toLocalTime().atDate(LocalDate.of(y, m, d))
            showChangedDateTime()
        }
        val dateDialog = DatePickerDialog(activity as Context, dateListener, this.dueDateTime!!.year, this.dueDateTime!!.month.value, this.dueDateTime!!.dayOfMonth)
        dateDialog.setTitle("Task Due Date")
        dateDialog.show()
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveAction(){

        // get data from layout
        val name = binding.tieName.text.toString()
        val description = binding.tieDescription.text.toString()
        val category = binding.tieCategory.text.toString()

        // create new task item or update existing
        if(taskItem == null){
            val newTask = TaskItem(name, description, this.dueDateTime, category)
            //TaskViewModel.addTaskItem(newTask)
            MainActivity.sqLiteManager?.addTaskItem(newTask)
        }
        else{
            MainActivity.sqLiteManager?.updateTaskItem(taskItem!!.id, name, description, this.dueDateTime, category)
            //TaskViewModel.updateTaskItem(taskItem!!.id, name, description, this.dueDateTime, category)
        }
        MainActivity.sqLiteManager?.loadToLocalMemory()

        // clear data on view
        binding.tieName.setText("")
        binding.tieDescription.setText("")
        binding.tieCategory.setText("")
        dismiss()
    }

    private fun deleteAction(){
        MainActivity.sqLiteManager?.deleteTaskItem(taskItem!!.id)
        MainActivity.sqLiteManager?.loadToLocalMemory()
        dismiss()
    }

}