package com.example.todomanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todomanager.databinding.ActivityMainBinding
import com.example.todomanager.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewTaskSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
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
        taskViewModel.name2.value = binding.tieName.text.toString()
        taskViewModel.description2.value = binding.tieDescription.text.toString()
        binding.tieName.setText("")
        binding.tieDescription.setText("")
        dismiss()
    }

}