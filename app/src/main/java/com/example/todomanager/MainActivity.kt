package com.example.todomanager

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todomanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TaskItemClickListener {
    companion object{
        var sqLiteManager: SQLiteManager? = null
    }


    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    //private lateinit var taskViewModel: SQLiteManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // to get back to main activity and show task edit view after on notification click
        val taskItem = intent.getSerializableExtra("taskItem") as? TaskItem
        if(taskItem != null)
            TaskSheet(this, taskItem).show(supportFragmentManager, "newTaskTag")

        // init sigleton
        sqLiteManager = SQLiteManager.instanceOfDatabase(this)
        //sqLiteManager?.clearTable()
        sqLiteManager?.loadToLocalMemory()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set up btn listener
        binding.fbtnNewTask.setOnClickListener{
            TaskSheet(this, null).show(supportFragmentManager, "newTaskTag")
        }
        binding.ibtnSettings.setOnClickListener {
            SettingsSheet(this).show(supportFragmentManager, "newSettingsTag")
        }

        binding.svTaskByName.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                sqLiteManager?.loadToLocalMemory()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0!!.isEmpty()){
                    sqLiteManager?.taskNameFilter = null
                    sqLiteManager?.loadToLocalMemory()
                }
                else sqLiteManager?.taskNameFilter = p0
                return true
            }

        })

        setRecyclerView()   // show tasks list on layout
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
        TaskSheet(this, taskItem).show(supportFragmentManager, "newTaskTag")
    }

    override fun changeTaskItemState(taskItem: TaskItem) {
        sqLiteManager?.toggleState(taskItem)
        sqLiteManager?.loadToLocalMemory()
    }
}