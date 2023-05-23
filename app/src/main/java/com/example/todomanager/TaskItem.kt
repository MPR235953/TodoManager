package com.example.todomanager

import android.content.Context
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class TaskItem{

    var name: String
    var description: String? = null
    var dueDateTime: LocalDateTime? = null
    var category: String? = null

    var id: String = UUID.randomUUID().toString()
    var createDateTime: LocalDateTime = LocalDateTime.now()
    var isDone: Int = 0
    var isNotification: Int = 0
    var isAttachment: Int = 0

    constructor(name: String, description: String?, dueDateTime: LocalDateTime?, category: String?,
    id:String = UUID.randomUUID().toString(), createDateTime:LocalDateTime = LocalDateTime.now(),
                isDone: Int = 0, isNotification: Int = 0, isAttachment: Int = 0 ){

        this.name = name
        this.description = description
        this.dueDateTime = dueDateTime
        this.category = category

        this.id = id
        this.createDateTime = createDateTime
        this.isDone = isDone
        this.isNotification = isNotification
        this.isAttachment = isAttachment
    }

    // functions to set appropriate image and color to done button
    fun imageResource(): Int =
        if(isDone == 1) R.drawable.ic_task_done_24
        else R.drawable.ic_task_not_done_24
    fun imageColor(context: Context): Int =
        if(isDone == 1) doneColor(context)
        else notDoneColor(context)

    // utils functions to get appropriate colors
    private fun notDoneColor(context: Context) =
        ContextCompat.getColor(context, R.color.purple_200)
    private fun doneColor(context: Context) =
        ContextCompat.getColor(context, R.color.teal_200)
}