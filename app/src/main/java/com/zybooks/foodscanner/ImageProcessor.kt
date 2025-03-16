@file:Suppress("DEPRECATION")

package com.zybooks.foodscanner

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images.Media.getBitmap
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector

fun convertURItoBitmap(uri: Uri, context: Context): Bitmap {
    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE // Force software rendering
            decoder.isMutableRequired = true
        }
    } else {
        getBitmap(context.contentResolver, uri) // Deprecated, but used for older versions
    }

    // Ensure bitmap is in ARGB_8888 format
    return bitmap.copy(Bitmap.Config.ARGB_8888, true)
}

fun debugPrint(results : List<Detection>) {
    for ((i, obj) in results.withIndex()) {
        val box = obj.boundingBox
        for ((j, category) in obj.categories.withIndex()) {
            val confidence: Int = category.score.times(100).toInt()
        }
    }
}


fun processImage(uri: Uri, context: Context) : List<Detection> {
    val image = convertURItoBitmap(uri, context)
    val tensorImage  = TensorImage.fromBitmap(image)
    val options = ObjectDetector.ObjectDetectorOptions.builder()
        .setMaxResults(15)
        .setScoreThreshold(0.01f)
        .build()
    val detector = ObjectDetector.createFromFileAndOptions(
            context, // the application context
    "model.tflite", // must be same as the filename in assets folder
    options
    )
    val results = detector.detect(tensorImage)
    debugPrint(results)
    return results
}