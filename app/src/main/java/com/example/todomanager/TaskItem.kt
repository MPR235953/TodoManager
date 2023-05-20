package com.example.todomanager

import android.content.Context
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class TaskItem(
    var name: String,
    var description: String?,
    var dueDateTime: LocalDateTime?,
    var category: String?,
    var isDone: Boolean = false,
    var isNotification: Boolean = false,
    var isAttachment: Boolean = false,
) {
    var id: UUID = UUID.randomUUID()
    var createDate: LocalDateTime = LocalDateTime.now()

    // functions to set appropriate image and color to done button
    fun imageResource(): Int =
        if(isDone) R.drawable.ic_task_done_24
        else R.drawable.ic_task_not_done_24
    fun imageColor(context: Context): Int =
        if(isDone) doneColor(context)
        else notDoneColor(context)

    // utils functions to get appropriate colors
    private fun notDoneColor(context: Context) =
        ContextCompat.getColor(context, R.color.purple_200)
    private fun doneColor(context: Context) =
        ContextCompat.getColor(context, R.color.teal_200)
}