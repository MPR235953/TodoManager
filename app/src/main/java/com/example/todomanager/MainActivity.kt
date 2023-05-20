package com.example.todomanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todomanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TaskItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        // set up btn listener
        binding.fbtnNewTask.setOnClickListener{
            NewTaskSheet(null).show(supportFragmentManager, "newTaskTag")
        }

        setRecyclerView()   // show tasks list on layout
    }

    fun setRecyclerView(){
        val mainActivity = this
        taskViewModel.taskItems.observe(mainActivity){
            binding.rvTodoList.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it, mainActivity)  // this cannot be used here
            }
        }
    }

    override fun editTaskItem(taskItem: TaskItem) {
        NewTaskSheet(taskItem).show(supportFragmentManager, "newTaskTag")
    }

    override fun completeTaskItem(taskItem: TaskItem) {
        taskViewModel.markAsCompleted(taskItem)
    }
}