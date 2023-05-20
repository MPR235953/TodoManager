package com.example.todomanager

import android.content.Context
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.util.*

class TaskItem {
    var id: UUID = UUID.randomUUID()
    var name: String
    var description: String
    lateinit var createDate: LocalDate
    var dueDate: LocalDate?
    lateinit var category: String
    var isTodo: Boolean = false
    var isNotification: Boolean = false
    var isAttachment: Boolean = false

    constructor(name: String, description: String, dueDate: LocalDate?, isTodo: Boolean){
        this.name = name
        this.description = description
        this.dueDate = dueDate
        this.isTodo = isTodo
    }

    // functions to set appropriate image and color to done button
    fun imageResource(): Int =
        if(isTodo) R.drawable.ic_task_not_done_24
        else R.drawable.ic_task_done_24
    fun imageColor(context: Context): Int =
        if(isTodo) todoColor(context)
        else doneColor(context)

    // utils functions to get appropriate colors
    private fun todoColor(context: Context) =
        ContextCompat.getColor(context, R.color.purple_200)
    private fun doneColor(context: Context) =
        ContextCompat.getColor(context, R.color.teal_200)
}