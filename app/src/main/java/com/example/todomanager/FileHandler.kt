package com.example.todomanager

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class FileHandler {
    fun copyFileToMyAppDir(contentResolver: ContentResolver, sourceUri: Uri): String{
        val targetFileName = getFileNameFromUri(contentResolver, sourceUri)
        val targetDirName = Environment.DIRECTORY_DOCUMENTS + "/TodoManagerAttachments"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, targetFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, contentResolver.getType(sourceUri))
            put(MediaStore.MediaColumns.RELATIVE_PATH, targetDirName)
        }

        var outputStream: OutputStream? = null
        var inputStream: InputStream? = null

        try {
            val collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val destinationUri = contentResolver.insert(collection, contentValues)
                ?: throw IOException("Failed to create destination URI")

            outputStream = contentResolver.openOutputStream(destinationUri)
                ?: throw IOException("Failed to open output stream")

            inputStream = contentResolver.openInputStream(sourceUri)
                ?: throw IOException("Failed to open input stream")

            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            return "$targetDirName/$targetFileName"
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                outputStream?.close()
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return "IOError"
    }


    private fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameColumnIndex = it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                if (displayNameColumnIndex != -1) {
                    return it.getString(displayNameColumnIndex)
                }
            }
        }
        // Default fallback if display name retrieval fails
        return "file"
    }


}