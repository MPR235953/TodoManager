package com.example.todomanager

import java.time.LocalTime

class TaskItem {
    lateinit var name: String
    lateinit var description: String
    lateinit var createDate: LocalTime
    lateinit var dueDate: LocalTime
    lateinit var category: String
    var isTodo: Boolean = false
    var isNotification: Boolean = false
    var isAttachment: Boolean = false
}