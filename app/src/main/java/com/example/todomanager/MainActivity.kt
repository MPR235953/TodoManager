package com.example.todomanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.todomanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        binding.fbtnNewTaskButton.setOnClickListener{
            NewTaskSheet().show(supportFragmentManager, "newTaskTag")
        }

        taskViewModel.name2.observe(this){
            binding.tvTaskName.text = String.format("Task Name %s", it)
        }

        taskViewModel.description2.observe(this){
            binding.tvTaskDescription.text = String.format("Task Description %s", it)
        }
    }
}