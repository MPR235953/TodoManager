package com.example.todomanager

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime
import java.util.UUID

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

        fun updateTaskItem(id: String, name: String, description: String?,
                           dueDateTime: LocalDateTime?, category: String?, isDone: Boolean = false,
                           isNotification: Boolean = false, isAttachment: Boolean = false){
            val list = taskItems.value
            val task = list?.find { it.id == id }!!
            task.name = name
            task.description = description
            task.dueDateTime = dueDateTime
            task.category = category
            task.isDone = isDone
            task.isNotification = isNotification
            task.isAttachment = isAttachment
            taskItems.postValue(list)
        }

        fun setDone(newTask: TaskItem){
            val list = taskItems.value
            val task = list?.find { it.id == newTask.id }!!
            task.isDone = true
            taskItems.postValue(list)
        }

    }

}