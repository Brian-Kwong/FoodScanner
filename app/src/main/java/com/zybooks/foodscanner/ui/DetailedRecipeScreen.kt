package com.zybooks.foodscanner.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zybooks.foodscanner.data.RecipeDetails

@Composable
fun RecipeImage(imageUrl: String, modifier : Modifier) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Recipe Image",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxWidth().height(300.dp)
    )
}



@Composable
fun IngredientEntry(ingredient: RecipeDetails.IngredientsDetails, index : Int) {
    Row (modifier = Modifier.fillMaxWidth()){
        Box(modifier = Modifier.weight(1f))
        {
            Text(text = index.toString(), fontWeight = FontWeight.Bold)
        }
        Box(modifier = Modifier.weight(5f))
        {
            Text(text = ingredient.name)
        }
        Box(modifier = Modifier.weight(2f))
        {
            Text(text = ingredient.unit)
        }
    }
}

@Composable
fun DetailEntry(title: String, value: String) {
    Row (modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.weight(1f))
        {
            Text(text = title, fontWeight = FontWeight.Bold)
        }
        Box(modifier = Modifier.weight(1f))
        {
            Text(text = value, maxLines = 5, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun DetailsPage(recipeDetails: RecipeDetails){
    LazyColumn {
        item {
            DetailEntry("Title", recipeDetails.title)
            DetailEntry("Summary", recipeDetails.summary)
            DetailEntry("Cuisine", if (recipeDetails.cuisines.isNotEmpty()) recipeDetails.cuisines.joinToString { ", " } else "N/A")
            DetailEntry("Dish Types", if (recipeDetails.dishTypes.isNotEmpty()) recipeDetails.dishTypes.joinToString { ", " } else "N/A")
            DetailEntry("Health Score", recipeDetails.healthScore.toString())
            DetailEntry("Ready in", recipeDetails.readyInMinutes.toString())
            DetailEntry("Servings", recipeDetails.servings.toString())
        }
    }
}

@Composable
fun IngredientsPage(recipeDetails: RecipeDetails) {
    Column {
        // Index followed by the name of the ingredient
        recipeDetails.extendedIngredients.forEach { ingredient ->
            IngredientEntry(ingredient, recipeDetails.extendedIngredients.indexOf(ingredient) + 1)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailedRecipeScreen(detailedRecipeViewModel: RecipeViewModel,modifier: Modifier, UpClick: () -> Unit = { }) {

    val selectedRecipe = detailedRecipeViewModel.selectedRecipeDetails.collectAsState().value
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Details", "Ingredients")


    if (detailedRecipeViewModel.loadingStatus.collectAsState().value || selectedRecipe === null) {
        LoadingScreen(modifier = modifier)
    }
    else {
        Scaffold(
        ) {
            Column(modifier = modifier.fillMaxWidth()) {
                RecipeTopBar(title = selectedRecipe.title, canNavigateBack = true, onUpClick = UpClick)
                RecipeImage(selectedRecipe.image, Modifier)
                TabRow(selectedTabIndex = tabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(text = { Text(title) },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index }
                        )
                    }
                }
                when (tabIndex) {
                    0 -> DetailsPage(selectedRecipe)
                    1 -> IngredientsPage(selectedRecipe)
                }
            }
        }
    }
}