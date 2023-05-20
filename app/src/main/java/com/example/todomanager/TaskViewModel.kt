package com.example.todomanager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.util.UUID

class TaskViewModel: ViewModel() {
    var taskItems = MutableLiveData<MutableList<TaskItem>>()  // list of tasks

    init{
        taskItems.value = mutableListOf()

        // Dummy content
        for (i in 1..10){
            addTaskItem(TaskItem("sth"+i,"desc", if(i % 2 == 0) LocalDate.now() else null, i % 2 == 0))
        }
    }

    fun addTaskItem(newTask: TaskItem){
        val list = taskItems.value
        list!!.add(newTask)
        taskItems.postValue(list)
    }

    fun updateTaskItem(id: UUID, name: String, description: String,
                       dueDate: LocalDate?, category: String = "", isTodo: Boolean = true,
                       isNotification: Boolean = false, isAttachment: Boolean = false){
        val list = taskItems.value
        val task = list?.find { it.id == id }!!
        task.name = name
        task.description = description
        task.dueDate = dueDate
        task.category = category
        task.isTodo = isTodo
        task.isNotification = isNotification
        task.isAttachment = isAttachment
        taskItems.postValue(list)
    }

    fun markAsCompleted(newTask: TaskItem){
        val list = taskItems.value
        val task = list?.find { it.id == newTask.id }!!
        task.isTodo = false
        taskItems.postValue(list)
    }

}