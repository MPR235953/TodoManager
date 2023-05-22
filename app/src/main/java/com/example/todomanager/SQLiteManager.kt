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
        values.put(DUE_DATETIME_COL, taskItem.dueDateTime!!.format(DateTimeFormatter.ofPattern("yy/MM/dd hh:mm")))
        values.put(CATEGORY_COL, taskItem.category)
        values.put(IS_DONE_COL, taskItem.isDone)
        values.put(IS_NOTIFICATION_COL, taskItem.isNotification)
        values.put(IS_ATTACHMENT_COL, taskItem.isAttachment)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun deleteTaskItem(taskItemId: Int) {
        val db = this.writableDatabase
        val whereClause = "$ID_COL = $taskItemId"
        val whereArgs = arrayOf(taskItemId.toString())

        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    fun updateTaskItem(taskItem: TaskItem) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(ID_COL, taskItem.id)
        values.put(NAME_COl, taskItem.name)
        values.put(DESCRIPTION_COL, taskItem.description)
        values.put(CREATE_DATETIME_COL, taskItem.createDateTime.format(DateTimeFormatter.ofPattern("yy/MM/dd hh:mm")))
        values.put(DUE_DATETIME_COL, taskItem.dueDateTime!!.format(DateTimeFormatter.ofPattern("yy/MM/dd hh:mm")))
        values.put(CATEGORY_COL, taskItem.category)
        values.put(IS_DONE_COL, taskItem.isDone)
        values.put(IS_NOTIFICATION_COL, taskItem.isNotification)
        values.put(IS_ATTACHMENT_COL, taskItem.isAttachment)

        val whereClause = "$ID_COL = ${taskItem.id}"
        val whereArgs = arrayOf(taskItem.id)

        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun populateNoteListArray() {
        val sqLiteDatabase = this.readableDatabase
        val result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)
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
                if(isDone == 0) false else true, if(isNotification == 0) false else true, if(isAttachment == 0) false else true)

            TaskViewModel.addTaskItem(taskItem)
        }


    }

    fun clearTable() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
        db.close()
    }
}