package com.example.todomanager

import android.content.ClipData
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class FileHandler {
    fun copyFileToMyAppDir(context:Context ,taskItem: TaskItem ,contentResolver: ContentResolver, sourceUri: Uri): String{
        val targetFileName = getFileNameFromUri(contentResolver, sourceUri)
        val targetDirName = Environment.DIRECTORY_DOCUMENTS + "/TodoManagerAttachments/task_${taskItem.id}"

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

            //var img: ByteArray? = getImageData(contentResolver, destinationUri)

            /*val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setDataAndType(destinationUri, contentResolver.getType(destinationUri))
            intent.clipData = ClipData.newRawUri(null, destinationUri)
            context.startActivity(intent)

            val test: Uri = Uri.parse("$targetDirName/$targetFileName")

            val res = "{usrPath: $targetDirName/$targetFileName, sysPath: $destinationUri}"

            val r = destinationUri.toString()*/

            return destinationUri.toString()
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

    fun getImageData(contentResolver: ContentResolver, fileUri: Uri): ByteArray? {
        // Read the file data using the content URI
        val inputStream = contentResolver.openInputStream(fileUri)
        inputStream?.use {
            return it.readBytes()
        }

        return null
    }


}