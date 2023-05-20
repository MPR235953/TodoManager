package com.example.todomanager

import java.time.LocalTime
import java.util.*

class TaskItem {
    var id: UUID = UUID.randomUUID()
    lateinit var name: String
    lateinit var description: String
    lateinit var createDate: LocalTime
    lateinit var dueDate: LocalTime
    lateinit var category: String
    var isTodo: Boolean = false
    var isNotification: Boolean = false
    var isAttachment: Boolean = false
}