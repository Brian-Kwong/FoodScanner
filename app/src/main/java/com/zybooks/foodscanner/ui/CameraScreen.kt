package com.zybooks.foodscanner.ui

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun CameraScreen(cameraScreenViewModel: CameraScreenViewModel){

    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            thumbnail: Bitmap? -> cameraScreenViewModel.photoBitmap = thumbnail
    }

    Column (modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        cameraScreenViewModel.photoBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(LocalConfiguration.current.screenWidthDp.dp)
            )
        }
            Spacer(Modifier.size(10.dp))
            if (cameraScreenViewModel.photoBitmap == null){
                Text("Take a picture of some of the ingredients!")
                Spacer(Modifier.size(10.dp))
                Button( onClick = { cameraLauncher.launch(null) }, shape = CircleShape, modifier = Modifier.size(100.dp)){
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "", modifier = Modifier.size(150.dp))
                }
            }
            else{
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
                    Button( onClick = { cameraScreenViewModel.handleSaveToFileSystem(context = context) }, shape = CircleShape, modifier = Modifier.size(100.dp)){
                        Icon(imageVector = Icons.Default.Check, contentDescription = "", modifier = Modifier.size(150.dp))
                    }
                    Button( onClick = { cameraLauncher.launch(null) }, shape = CircleShape, modifier = Modifier.size(100.dp)){
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "", modifier = Modifier.size(150.dp))
                    }
            }
        }


    }
}
