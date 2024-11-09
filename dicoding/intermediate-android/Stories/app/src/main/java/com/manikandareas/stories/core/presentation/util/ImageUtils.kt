package com.manikandareas.stories.core.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
        val file = File(context.cacheDir, "image.jpg")
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return Uri.fromFile(file)
    }

    fun uriToFile(uri: Uri, context: Context): File? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_image.jpg")
        try {
            FileOutputStream(file).use { output ->
                inputStream?.copyTo(output)
            }
            return file
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}