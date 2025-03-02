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
import com.zybooks.foodscanner.processImage

@Composable
fun ImagePicker(){

    var imageUUri  by remember  { mutableStateOf("") }
    val context = LocalContext.current
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            // Returns the URI of the selected media item
            processImage(uri, context)

        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    val mimeType = "image/*"

    // Button for selecting an image
    Button(onClick = {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType)))
        Log.d("PhotoPicker2", imageUUri)
    }) {
        Text("Select Image")
    }
}