package com.zybooks.foodscanner.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun RecipeImage(imageUrl: String, modifier : Modifier) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Recipe Image",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxWidth().height(300.dp)
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailedRecipeScreen(detailedRecipeViewModel: RecipeViewModel,modifier: Modifier, UpClick: () -> Unit = { }) {

    val selectedRecipe = detailedRecipeViewModel.selectedRecipeDetails.collectAsState().value
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Details", "About", "Settings")


    if (detailedRecipeViewModel.loadingStatus.collectAsState().value || selectedRecipe === null) {
        LoadingScreen(modifier = modifier)
    }
    else {
        Scaffold(
        ) {
            Column(modifier = modifier.fillMaxWidth()) {
                RecipeTopBar(title = selectedRecipe.title, canNavigateBack = true, onUpClick = {})
                RecipeImage(selectedRecipe.image, Modifier)
                Text("Recipe Name" + selectedRecipe?.title, Modifier)
                Text("Recipe Description" + selectedRecipe?.summary, Modifier)
            }
        }
    }
}