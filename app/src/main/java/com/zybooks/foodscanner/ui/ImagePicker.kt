package com.zybooks.foodscanner.ui

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.zybooks.foodscanner.data.Ingredients
import com.zybooks.foodscanner.processImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    val imageUUri  by remember  { mutableStateOf("") }


    val context = LocalContext.current
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the picker
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            // Returns the URI of the selected media item
                val results = processImage(uri, context)
                navigateToIngredients(processDetection(results))
        } else {
            // Returns null if the user didn't select any media
            Log.d("PhotoPicker", "No media selected")
        }
    }

    val fileType = "image/*"

    // Button for selecting an image
    Button(onClick = {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType(fileType)))
    }) {
        Text("Select Image")
    }
}