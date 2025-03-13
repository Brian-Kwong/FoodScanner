package com.zybooks.foodscanner.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zybooks.foodscanner.processImage
import java.io.File
import java.util.Date

class CameraScreenViewModel: ViewModel() {

    var photoBitmap: Bitmap? by mutableStateOf(null)

    fun handleSaveToFileSystem(context: Context){
        val fileName = "ingredients-${Date().time}.jpg"
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { stream -> photoBitmap?.compress(Bitmap.CompressFormat.JPEG, 95, stream) }
        processImage(Uri.fromFile(File(context.filesDir.toString(), fileName)), context)
    }
}