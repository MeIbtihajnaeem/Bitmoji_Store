package com.example.myapplication.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


object imageUtils {
    fun saveImageBitmap(
        context: Context,
        bitmap: Bitmap,
        name: String,
        extension: String
    ) {
        var name = name
        name = "$name.$extension"
        val directory =
            context.getDir("imageDir", Context.MODE_APPEND)
        // Create imageDir
        val mypath = File(directory, name)
        var fileOutputStream: FileOutputStream? = null
        Log.i("My Path ", mypath.toString())
        try {
            fileOutputStream = FileOutputStream(mypath)
            // fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            val temp =
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream)
            Log.i("Image Value ", temp.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("My message ", e.toString())
        } finally {
            try {
                fileOutputStream!!.close()
                Log.i("My message ", "I am in try ")
            } catch (e: IOException) {
                e.printStackTrace()
                Log.i("My message ", e.toString())
            }
        }
    }

    fun loadImageBitmap(
        context: Context,
        name: String,
        extension: String
    ): Bitmap? {
        var name = name
        val directory =
            context.getDir("imageDir", Context.MODE_APPEND)
        // Create imageDir
        name = "$name.$extension"
        val mypath = File(directory, name)
        Log.i("Load Path", mypath.toString())
        var fileInputStream: FileInputStream? = null
        var bitmap: Bitmap? = null
        try {
            fileInputStream = FileInputStream(mypath)
            //fileInputStream = context.openFileInput(name);
            bitmap = BitmapFactory.decodeStream(fileInputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileInputStream!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return bitmap
    }
}