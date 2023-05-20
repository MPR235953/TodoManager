package com.example.todomanager

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todomanager.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        if(taskItem != null){
            binding.tvTaskTitle.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            binding.tieName.text = editable.newEditable(taskItem!!.name)
            binding.tieDescription.text = editable.newEditable(taskItem!!.description)
        }
        else binding.tvTaskTitle.text = "Edit Task"

        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)
        binding.btnSave.setOnClickListener{
            saveAction()
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveAction(){
        val name = binding.tieName.text.toString()
        val description = binding.tieDescription.text.toString()

        if(taskItem == null){
            val newTask = TaskItem(name, description, null, true)
            taskViewModel.addTaskItem(newTask)
        }
        else{
            taskViewModel.updateTaskItem(taskItem!!.id, name, description, null)
        }

        binding.tieName.setText("")
        binding.tieDescription.setText("")
        dismiss()
    }

}