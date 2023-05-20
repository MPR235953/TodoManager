package com.example.todomanager

import android.app.DatePickerDialog
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
import java.time.format.DateTimeFormatter

class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueDate: LocalDate? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        // decide what will be seen when add button was clicked
        if(taskItem != null){
            binding.tvTaskTitle.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            binding.tieName.text = editable.newEditable(taskItem!!.name)
            binding.tieDescription.text = editable.newEditable(taskItem!!.description)
            if(taskItem!!.dueDate != null){
                dueDate = taskItem!!.dueDate!!
                updateDateOnDataPickerButton()
            }
        }
        else binding.tvTaskTitle.text = "New Task"

        // save new task
        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)
        binding.btnSave.setOnClickListener{
            saveAction()
        }
        binding.btnPickDueDate.setOnClickListener{
            openDatePicker()
        }
    }

    private fun updateDateOnDataPickerButton() {
        binding.btnPickDueDate.text = dueDate!!.format(DateTimeFormatter.ofPattern("yy/MM/dd"))
    }

    private fun openDatePicker(){
        if(dueDate == null) dueDate = LocalDate.now()
        val listener = DatePickerDialog.OnDateSetListener{ _, selectedYear, selectedMonth, selectedDay ->
            dueDate = LocalDate.of(selectedYear, selectedMonth, selectedDay)
            updateDateOnDataPickerButton()
        }
        val dialog = DatePickerDialog(activity as Context, listener, dueDate!!.year, dueDate!!.month.value, dueDate!!.dayOfMonth)
        dialog.setTitle("Task Due Date")
        dialog.show()
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveAction(){
        // get data from layout
        val name = binding.tieName.text.toString()
        val description = binding.tieDescription.text.toString()

        // create new task item or update existing
        if(taskItem == null){
            val newTask = TaskItem(name, description, dueDate, true)
            taskViewModel.addTaskItem(newTask)
        }
        else{
            taskViewModel.updateTaskItem(taskItem!!.id, name, description, dueDate)
        }

        // clear data on view
        binding.tieName.setText("")
        binding.tieDescription.setText("")
        dismiss()
    }

}