package com.zybooks.foodscanner.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zybooks.foodscanner.data.Recipe
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.zybooks.foodscanner.R
import com.zybooks.foodscanner.data.RecipeAPI
import com.zybooks.foodscanner.data.RecipeAPIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Create a card component that will display search resiults
// On the left side would be the image of the recipe
// On the right will be  a column with the title of the recipe, and details about the meail each on their own row

@Composable
fun CreateRecipeCard(
    recipe: Recipe,
    apiRouter : RecipeAPIService,
    apiKey : String,
    corountine: CoroutineScope,
    modifier: Modifier
) {

    Card(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize(),
        onClick = {

            corountine.launch {
                val recipeDetails = apiRouter.getRecipeInformation(apiKey, recipe.id.toString())
                Log.d("RecipeTableScreen", "Recipe Details: $recipeDetails")
            }

        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight().padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically // Add this line for vertical centering
        ) {
            RecipeImage(recipe = recipe)
            Spacer(modifier = Modifier.size(10.dp))
            Column {
                Text(text = recipe.title, fontWeight = FontWeight.Bold)
                Text(text = "Available ingredients ${recipe.usedIngredients.size} Missing ingredients ${recipe.missedIngredients.size}")
            }
        }
    }
}

@Composable
fun RecipeImage(recipe: Recipe) {
    AsyncImage(
        model = recipe.image,
        contentDescription = "Recipe Image",
        modifier = Modifier
            .size(120.dp) // Give the image a fixed size
            .clip(RoundedCornerShape(8.dp)) // Clip the image to be rounded
    )
}

@Composable
fun LoadingScreen(modifier: Modifier){
    Column(modifier = modifier){
        Text(text = "Loading...")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeTopBar(
    title: String,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    onUpClick: () -> Unit = { },
) {
    TopAppBar(
        title = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = title)

            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = onUpClick,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}



@Composable
fun RecipeTableScreen(modifier: Modifier, viewModel: RecipeViewModel, onUpClick: () -> Unit) {

    val apiKey = stringResource(R.string.api_key)
    val couroutine = remember  { CoroutineScope(Dispatchers.IO) }

    val recipeAPIService: RecipeAPI by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/")
            .build()
        retrofit.create(RecipeAPI::class.java)
    }

    val recipeAPI = RecipeAPIService(recipeAPIService)

    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        if (viewModel.loadingStatus.collectAsState().value || viewModel.recipes.isEmpty()) {
            LoadingScreen(modifier = modifier)
        } else {
            RecipeTopBar(title = "Recipes", canNavigateBack = true, onUpClick = {})
            // Center Text
            LazyColumn(modifier = modifier.fillMaxSize()) {
                items(viewModel.recipes.size) { index ->
                    CreateRecipeCard(recipe = viewModel.recipes[index], recipeAPI,apiKey,couroutine, modifier = modifier)
                }
            }
        }
    }
}
