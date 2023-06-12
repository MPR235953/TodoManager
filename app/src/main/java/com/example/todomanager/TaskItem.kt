package com.example.todomanager

import android.content.Context
import androidx.core.content.ContextCompat
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class TaskItem: Serializable{

    var name: String
    var description: String? = null
    var dueDateTime: LocalDateTime? = null
    var category: String? = null

    var id: Long = -1
    var createDateTime: LocalDateTime = LocalDateTime.now()
    var isDone: Int = 0
    var isNotification: Int = 0
    var isAttachment: Int = 0

    constructor(name: String, description: String?, dueDateTime: LocalDateTime?, category: String?,
    id: Long = -1, createDateTime:LocalDateTime = LocalDateTime.now(),
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
    fun imageResourceForIsDoneButton(isDone: Int? = null): Int {
        val flag = if (isDone != null) isDone else this.isDone
        if (flag == 1) return R.drawable.ic_task_done_24
        else return R.drawable.ic_task_not_done_24
    }
    fun imageColorForIsDoneButton(context: Context, isDone: Int? = null): Int {
        val flag = if (isDone != null) isDone else this.isDone
        if (flag == 1) return ContextCompat.getColor(context, R.color.teal_200)
        else return ContextCompat.getColor(context, R.color.purple_200)
    }

    fun imageColorForIsNotificationButton(context: Context, isNotification: Int? = null): Int {
        val flag = if (isNotification != null) isNotification else this.isNotification
        if (flag == 1) return ContextCompat.getColor(context, R.color.teal_200)
        else return ContextCompat.getColor(context, R.color.purple_200)
    }

    fun imageColorForAddAttachmentButton(context: Context, isAttachment: Int? = null): Int {
        val flag = if (isAttachment != null) isAttachment else this.isAttachment
        if (flag == 1) return ContextCompat.getColor(context, R.color.teal_200)
        else return ContextCompat.getColor(context, R.color.purple_200)
    }

    companion object{
        fun previewImageResource(active: Int): Int {
            if (active == 1) return R.drawable.ic_task_done_24
            else return R.drawable.ic_task_not_done_24
        }
        fun previewImageColor(context: Context, active: Int): Int {
            if (active == 1) return ContextCompat.getColor(context, R.color.teal_200)
            else return ContextCompat.getColor(context, R.color.purple_200)
        }
    }

}