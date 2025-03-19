package com.zybooks.foodscanner.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zybooks.foodscanner.processImage
import org.tensorflow.lite.task.vision.detector.Detection

fun processDetection(results : List<Detection>) : String {
    val filteredResults = results.filter {
        // Filter out results with low confidence
        (it.categories.firstOrNull()?.score ?: 0.0f) > 0.15f
    }.map {
        // Get the category for each result
        val label = it.categories.firstOrNull()?.label ?: "Unknown"
        label.split("-")[0] // Split to get the main
    }.filter {
        // Filter unwanted labels
        it != "Unknown"
    }

    // Convert the list of labels to a map with counts
    // Sort by count descending and take the top 3
    // Take the first 3 labels to avoid too many results
    val resultCount = filteredResults.groupingBy { it }.eachCount().entries.sortedBy {
        it.value
    }.reversed().take(3).associate {
        it.key to it.value
    }.toString().replace("{", "").replace("}", "")
    return resultCount
}

@Composable
fun ImagePicker( navigateToIngredients: (String) -> Unit = { }) {

    val context = LocalContext.current
    var hasFSAccess = remember { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    }
    else{
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }}

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the picker
        if (uri != null) {
            // Returns the URI of the selected media item
            val results = processImage(uri, context)
            navigateToIngredients(processDetection(results))
        } else {
            // Returns null if the user didn't select any media
        }
    }

    // Request camera permission if not granted
    val requestUserForFSAccess = rememberLauncherForActivityResult (
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            hasFSAccess = true
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            // Check if they have permanently denied the permission
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.CAMERA
                )
            ) {
                // Show toast this feature won't work without the permission
                Toast.makeText(
                    context,
                    "FS permission is required to select media.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }



    // Button for selecting an image
    Button(onClick = {
        if (!hasFSAccess &&Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            requestUserForFSAccess.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            return@Button
        } else {
            // Android 13 doesn't require permission for media access of images
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }) {
        Text("Through a Image")
    }
}