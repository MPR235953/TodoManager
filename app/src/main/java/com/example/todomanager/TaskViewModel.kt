package com.example.todomanager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalTime
import java.util.UUID

class TaskViewModel: ViewModel() {
    var taskItems = MutableLiveData<MutableList<TaskItem>>()

    init{
        taskItems.value = mutableListOf()
    }

    fun addTaskItem(newTask: TaskItem){
        val list = taskItems.value
        list!!.add(newTask)
        taskItems.postValue(list)
    }

    fun updateTaskItem(id: UUID, name: String, description: String,
                       dueDate: LocalTime?, category: String = "", isTodo: Boolean = true,
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