package com.example.todomanager

import androidx.lifecycle.MutableLiveData

class TaskViewModel {
    companion object {
        var taskItems = MutableLiveData<MutableList<TaskItem>>()  // list of tasks

        init{
            taskItems.value = mutableListOf()
        }

        fun addTaskItem(newTask: TaskItem){
            val list = taskItems.value
            list!!.add(newTask)
            taskItems.postValue(list)
        }

    }

}