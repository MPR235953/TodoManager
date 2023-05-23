package com.example.todomanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.ViewModelProvider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
        private const val IS_ATTACHMENT_COL = "is_attachment"
    }

    var isDoneFilter: Int? = null
    var categoryFilter: String? = null
    var notifyDelay: Int? = null

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " TEXT PRIMARY KEY, " +
                NAME_COl + " TEXT," +
                DESCRIPTION_COL + " TEXT," +
                CREATE_DATETIME_COL + " TEXT," +
                DUE_DATETIME_COL + " TEXT," +
                CATEGORY_COL + " TEXT," +
                IS_DONE_COL + " INTEGER," +
                IS_NOTIFICATION_COL + " INTEGER," +
                IS_ATTACHMENT_COL + " INTEGER" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun addTaskItem(taskItem: TaskItem){
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(ID_COL, taskItem.id)
        values.put(NAME_COl, taskItem.name)
        values.put(DESCRIPTION_COL, taskItem.description)
        values.put(CREATE_DATETIME_COL, taskItem.createDateTime.format(DateTimeFormatter.ofPattern("yy/MM/dd hh:mm")))
        if(taskItem.dueDateTime != null) values.put(DUE_DATETIME_COL, taskItem.dueDateTime!!.format(DateTimeFormatter.ofPattern("yy/MM/dd hh:mm")))
        values.put(CATEGORY_COL, taskItem.category)
        values.put(IS_DONE_COL, taskItem.isDone)
        values.put(IS_NOTIFICATION_COL, taskItem.isNotification)
        values.put(IS_ATTACHMENT_COL, taskItem.isAttachment)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun deleteTaskItem(taskItemId: String) {
        val db = this.writableDatabase
        val whereClause = "$ID_COL = ?"
        val whereArgs = taskItemId

        db.delete(TABLE_NAME, whereClause, arrayOf(whereArgs))
        db.close()
    }

    fun updateTaskItem(id: String, name: String, description: String?,
                       dueDateTime: LocalDateTime?, category: String?, isDone: Int = 0,
                       isNotification: Int = 0, isAttachment: Int = 0) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(NAME_COl, name)
        values.put(DESCRIPTION_COL, description)
        values.put(DUE_DATETIME_COL, dueDateTime!!.format(DateTimeFormatter.ofPattern("yy/MM/dd hh:mm")))
        values.put(CATEGORY_COL, category)
        values.put(IS_DONE_COL, isDone)
        values.put(IS_NOTIFICATION_COL, isNotification)
        values.put(IS_ATTACHMENT_COL, isAttachment)

        val whereClause = "$ID_COL = ?"
        val whereArgs = arrayOf(id)

        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun loadToLocalMemory() {
        TaskViewModel.taskItems.value = mutableListOf()
        val sqLiteDatabase = this.readableDatabase

        var query: String = ""
        if(isDoneFilter == null && categoryFilter == null) query = "SELECT * FROM $TABLE_NAME ORDER BY $DUE_DATETIME_COL ASC"
        else if (isDoneFilter != null && categoryFilter == null) query = "SELECT * FROM $TABLE_NAME WHERE $IS_DONE_COL = $isDoneFilter ORDER BY $DUE_DATETIME_COL ASC"
        else if (isDoneFilter == null && categoryFilter != null) query = "SELECT * FROM $TABLE_NAME WHERE $CATEGORY_COL = '$categoryFilter' ORDER BY $DUE_DATETIME_COL ASC"
        else query = "SELECT * FROM $TABLE_NAME WHERE $IS_DONE_COL = $isDoneFilter AND $CATEGORY_COL = '$categoryFilter' ORDER BY $DUE_DATETIME_COL ASC"

        val result = sqLiteDatabase.rawQuery(query, null)
        for (i in 0 until result.count) {
            result.moveToPosition(i)
            val id = result.getString(0)
            val title = result.getString(1)
            val desc = result.getString(2)
            val createDateTime = result.getString(3)
            val dueDateTime = result.getString(4)
            val category = result.getString(5)
            val isDone = result.getInt(6)
            val isNotification = result.getInt(7)
            val isAttachment = result.getInt(8)

            val taskItem = TaskItem(title, desc, DataTimeConverter.string2DateTime(dueDateTime), category,
                id, DataTimeConverter.string2DateTime(createDateTime),
                isDone, isNotification, isAttachment)

            TaskViewModel.addTaskItem(taskItem)
        }
    }

    fun toggleState(taskItem: TaskItem){
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(IS_DONE_COL, if (taskItem.isDone == 1) 0 else 1)

        val whereClause = "$ID_COL = ?"
        val whereArgs = arrayOf(taskItem.id)

        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun clearTable() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
        db.close()
    }
}