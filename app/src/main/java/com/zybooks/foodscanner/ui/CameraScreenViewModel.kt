package com.zybooks.foodscanner.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zybooks.foodscanner.processImage
import org.tensorflow.lite.task.vision.detector.Detection
import java.io.File
import java.util.Date

class CameraScreenViewModel: ViewModel() {


    var photoBitmap: Bitmap? by mutableStateOf(null)
    var hasCameraPermission by mutableStateOf(false)

    fun handleSaveToFileSystem(context: Context) : List<Detection>{
        val fileName = "ingredients-${Date().time}.jpg"
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { stream -> photoBitmap?.compress(Bitmap.CompressFormat.JPEG, 95, stream) }
        return processImage(Uri.fromFile(File(context.filesDir.toString(), fileName)), context)
    }

}