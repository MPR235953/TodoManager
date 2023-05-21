package com.example.todomanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todomanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TaskItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    //private lateinit var taskViewModel: SQLiteManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFromDBToMemory()

        //taskViewModel = SQLiteManager(this, null)
        // set up btn listener
        binding.fbtnNewTask.setOnClickListener{
            NewTaskSheet(this, null).show(supportFragmentManager, "newTaskTag")
        }

        setRecyclerView()   // show tasks list on layout
    }

    private fun loadFromDBToMemory() {
        val sqLiteManager: SQLiteManager? = SQLiteManager.instanceOfDatabase(this)
        sqLiteManager?.populateNoteListArray()
    }

    fun setRecyclerView(){
        val mainActivity = this
        TaskViewModel.taskItems.observe(mainActivity){
            binding.rvTodoList.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it, mainActivity)  // this cannot be used here
            }
        }
    }

    override fun editTaskItem(taskItem: TaskItem) {
        NewTaskSheet(this, taskItem).show(supportFragmentManager, "newTaskTag")
    }

    override fun completeTaskItem(taskItem: TaskItem) {
        TaskViewModel.setDone(taskItem)
        // update db
    }
}