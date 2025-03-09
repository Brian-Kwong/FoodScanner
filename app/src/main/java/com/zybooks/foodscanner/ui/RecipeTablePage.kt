package com.zybooks.foodscanner.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zybooks.foodscanner.data.Recipe

// Create a card component that will display search resiults
// On the left side would be the image of the recipe
// On the right will be  a column with the title of the recipe, and details about the meail each on their own row

@Composable
fun CreateRecipeCard(
    recipe: Recipe,
    modifier: Modifier
) {

    Card {
        Column {
            RecipeImage(recipe = recipe)
             Column {
                 Text(text = recipe.title)
                 Text(text = "Used in ${recipe.usedIngredients} ingredients")
             }
         }
    }
}

@Composable
fun RecipeImage(recipe: Recipe) {
    AsyncImage(
        model = recipe.image,
        contentDescription = "Recipe Image",
        modifier = Modifier.size(100.dp)
    )

}

@Composable
fun RecipeTableScreen(modifier: Modifier, viewModel: RecipeViewModel) {
    // Center Text
    Text(text = "Recipes", modifier = modifier)
    Column(modifier = modifier) {
        viewModel.recipes.forEach { recipe ->
            CreateRecipeCard(recipe = recipe, modifier = Modifier)
        }
    }
}
