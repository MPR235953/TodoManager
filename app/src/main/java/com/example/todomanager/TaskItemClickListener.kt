package com.example.todomanager

interface TaskItemClickListener {
    fun editTaskItem(taskItem: TaskItem)
    fun changeTaskItemState(taskItem: TaskItem)
}