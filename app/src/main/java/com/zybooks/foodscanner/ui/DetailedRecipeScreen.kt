package com.zybooks.foodscanner.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zybooks.foodscanner.data.RecipeDetails
import java.util.Locale
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.wear.compose.foundation.SwipeToDismissValue

@Composable
fun RecipeImage(imageUrl: String, modifier : Modifier) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Recipe Image",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}


@Composable
fun InstructionsEntry(instruction : RecipeDetails.Steps, index : Int, modifier: Modifier = Modifier) {

    val ingredients =  if (instruction.ingredients.isNotEmpty()) instruction.ingredients.map{
        it.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }.joinToString(", " ) else "No ingredients"


    Card (modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f) )
            {
                Text(text = index.toString(), fontWeight = FontWeight.Bold)
            }
            Box(modifier = Modifier.weight(5f))
            {
                Column {
                    Text(text = ingredients)
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = instruction.step)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun InstructionsPage(recipeDetails: RecipeDetails) {

    val instructions = remember {  mutableStateListOf(*recipeDetails.analyzedInstructions.flatMap { it.steps }.toTypedArray()) }

    val removeItem = { item : RecipeDetails.Steps ->
        instructions.remove(item)
    }


    if (instructions.isEmpty()) {
        // Center on Screen
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Done! ＼（＾○＾）人（＾○＾）／", style = MaterialTheme.typography.bodyMedium)
        }
    }
    else {

        LazyColumn {
            items(
                count = instructions.size,
                key = { index -> instructions[index].step }
            ) { index ->
                val step = instructions[index]
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->
                        when (value) {
                            SwipeToDismissBoxValue.EndToStart -> {
                                removeItem(step)
                                true
                            }

                            else -> false
                        }
                    }
                )
                SwipeToDismissBox(
                    state = dismissState,
                    content = {
                        InstructionsEntry(step, index + 1)
                    },
                    backgroundContent = {},
                    modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
                    enableDismissFromStartToEnd = false,
                    enableDismissFromEndToStart = true
                )
            }
        }
    }
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
    var tabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf("Details", "Ingredients", "Instructions")


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
                    2 -> InstructionsPage(selectedRecipe)
                }
            }
        }
    }
}