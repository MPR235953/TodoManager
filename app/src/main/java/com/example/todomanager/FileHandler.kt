package com.example.todomanager

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class FileHandler(var context: Context) {

    val AppDir = File(this.context.getExternalFilesDir(null), "TodoManagerDir")
    init { this.AppDir.mkdirs() }

    fun copyToAppDir(src: String): String{
        val inputStream: InputStream? = context.contentResolver.openInputStream(src.toUri())
        val parts = src.split(".")
        val aName = "a_${System.currentTimeMillis()}.${parts[parts.size-1]}"

        val file = File(this.AppDir, aName)
        val outputStream = context.contentResolver.openOutputStream(file.toUri())

        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
            outputStream?.write(buffer, 0, bytesRead)
        }

        return aName
    }

    fun generateFileFromName(aName: String): File{
        return File(this.AppDir, aName)
    }

    fun removeFiles(fileList: MutableList<String>){
        for(file in fileList)
            File(this.AppDir, file).delete()
    }

}