package com.example.todomanager

import androidx.lifecycle.MutableLiveData

class TaskViewModel {
    companion object {
        var taskItems = MutableLiveData<MutableList<TaskItem>>()  // list of tasks

        init{
            taskItems.value = mutableListOf()

            // Dummy content
            /*for (i in 1..10){
                addTaskItem(TaskItem("task "+i,"desc", if(i % 2 == 0) LocalDateTime.now() else null, "Default", isDone = i % 2 == 0))
            }*/
        }

        fun addTaskItem(newTask: TaskItem){
            val list = taskItems.value
            list!!.add(newTask)
            taskItems.postValue(list)
        }

    }

}