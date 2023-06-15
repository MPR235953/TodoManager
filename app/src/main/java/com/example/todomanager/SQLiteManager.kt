package com.example.todomanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDateTime


class SQLiteManager(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        var sqLiteManager: SQLiteManager? = null

        fun instanceOfDatabase(context: Context?): SQLiteManager? {
            if (sqLiteManager == null) sqLiteManager = SQLiteManager(context)

            return sqLiteManager
        }

        private val DATABASE_NAME = "TODO_DB"
        private val DATABASE_VERSION = 1

        private const val TABLE_NAME = "todos"
        private const val ID_COL = "id"
        private const val NAME_COl = "name"
        private const val DESCRIPTION_COL = "description"
        private const val CREATE_DATETIME_COL = "create_datetime"
        private const val DUE_DATETIME_COL = "due_datetime"
        private const val CATEGORY_COL = "category"
        private const val IS_DONE_COL = "is_done"
        private const val IS_NOTIFICATION_COL = "is_notification"
        private const val ATTACHMENTS_COL = "attachments"
    }

    var isDoneFilter: Int? = null
    var categoryFilter: String? = null
    var notifyDelay: Int? = null
    var taskNameFilter: String? = null

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COl + " TEXT," +
                DESCRIPTION_COL + " TEXT," +
                CREATE_DATETIME_COL + " TEXT," +
                DUE_DATETIME_COL + " TEXT," +
                CATEGORY_COL + " TEXT," +
                IS_DONE_COL + " INTEGER," +
                IS_NOTIFICATION_COL + " INTEGER," +
                ATTACHMENTS_COL + " TEXT" + ")")

        db.execSQL(query)
    }

    fun updateNotifications(context: Context?){
        val notificationHandler = NotificationHandler(context!!)
        notificationHandler.createNotificationChannel()

        val query = "SELECT * FROM $TABLE_NAME WHERE $IS_NOTIFICATION_COL = 1 AND $IS_DONE_COL = 0 AND $DUE_DATETIME_COL > '${DataTimeConverter.dateTime2String(LocalDateTime.now())}'"
        val sqLiteDatabase = this.readableDatabase
        val result = sqLiteDatabase.rawQuery(query, null)
        for (i in 0 until result.count) {
            result.moveToPosition(i)
            val id = result.getLong(0)
            val title = result.getString(1)
            val desc = result.getString(2)
            val createDateTime = result.getString(3)
            val dueDateTime = result.getString(4)
            val category = result.getString(5)
            val isDone = result.getInt(6)
            val isNotification = result.getInt(7)
            val attachments = result.getString(8)

            val taskItem = TaskItem(title, desc, DataTimeConverter.string2DateTime(dueDateTime), category,
                id, DataTimeConverter.string2DateTime(createDateTime),
                isDone, isNotification, attachments)

            notificationHandler.deleteNotification(taskItem)
            notificationHandler.createNotification(taskItem)
        }
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun addTaskItem(taskItem: TaskItem){
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(NAME_COl, taskItem.name)
        values.put(DESCRIPTION_COL, taskItem.description)
        values.put(CREATE_DATETIME_COL, DataTimeConverter.dateTime2String(taskItem.createDateTime))
        if(taskItem.dueDateTime != null) values.put(DUE_DATETIME_COL, DataTimeConverter.dateTime2String(taskItem.dueDateTime!!))
        values.put(CATEGORY_COL, taskItem.category)
        values.put(IS_DONE_COL, taskItem.isDone)
        values.put(IS_NOTIFICATION_COL, taskItem.isNotification)
        values.put(ATTACHMENTS_COL, taskItem.attachments)

        val taskId = db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun deleteTaskItem(taskItemId: Long) {
        val db = this.writableDatabase
        val whereClause = "$ID_COL = ?"
        val whereArgs = taskItemId

        db.delete(TABLE_NAME, whereClause, arrayOf(whereArgs.toString()))
        db.close()
    }

    fun isNameUnique(name: String): Boolean{
        val sqLiteDatabase = this.readableDatabase
        val query = "SELECT $NAME_COl FROM $TABLE_NAME"
        val result = sqLiteDatabase.rawQuery(query, null)
        for (i in 0 until result.count) {
            result.moveToPosition(i)
            if(name.equals(result.getString(0)))
                return false
        }
        return true
    }

    fun updateTaskItem(id: Long, name: String, description: String?,
                       dueDateTime: LocalDateTime?, category: String?, isDone: Int = 0,
                       isNotification: Int = 0, attachments: String = "") {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(NAME_COl, name)
        values.put(DESCRIPTION_COL, description)
        values.put(DUE_DATETIME_COL, DataTimeConverter.dateTime2String(dueDateTime!!))
        values.put(CATEGORY_COL, category)
        values.put(IS_DONE_COL, isDone)
        values.put(IS_NOTIFICATION_COL, isNotification)
        values.put(ATTACHMENTS_COL, attachments)

        val whereClause = "$ID_COL = ?"
        val whereArgs = arrayOf(id.toString())

        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun loadToLocalMemory() {
        TaskViewModel.taskItems.value = mutableListOf()
        val sqLiteDatabase = this.readableDatabase

        var query: String = ""
        if(isDoneFilter == null && categoryFilter == null && taskNameFilter == null)
            query = "SELECT * FROM $TABLE_NAME ORDER BY $DUE_DATETIME_COL ASC"
        else{
            query = "SELECT * FROM $TABLE_NAME WHERE "
            if(isDoneFilter != null) query += "$IS_DONE_COL = $isDoneFilter AND "
            if(categoryFilter != null) query += "$CATEGORY_COL = '$categoryFilter' AND "
            if(taskNameFilter != null) query += "$NAME_COl LIKE '$taskNameFilter%' AND "
            query = query.dropLast(5)
            query += " ORDER BY $DUE_DATETIME_COL ASC"
        }

        val result = sqLiteDatabase.rawQuery(query, null)
        for (i in 0 until result.count) {
            result.moveToPosition(i)
            val id = result.getLong(0)
            val title = result.getString(1)
            val desc = result.getString(2)
            val createDateTime = result.getString(3)
            val dueDateTime = result.getString(4)
            val category = result.getString(5)
            val isDone = result.getInt(6)
            val isNotification = result.getInt(7)
            val attachments = result.getString(8)

            val taskItem = TaskItem(title, desc, DataTimeConverter.string2DateTime(dueDateTime), category,
                id, DataTimeConverter.string2DateTime(createDateTime),
                isDone, isNotification, attachments)

            TaskViewModel.addTaskItem(taskItem)
        }
    }

    fun toggleState(taskItem: TaskItem){
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(IS_DONE_COL, if (taskItem.isDone == 1) 0 else 1)

        val whereClause = "$ID_COL = ?"
        val whereArgs = arrayOf(taskItem.id.toString())

        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun clearTable() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
        db.close()
    }
}