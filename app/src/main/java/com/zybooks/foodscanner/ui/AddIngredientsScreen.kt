package com.zybooks.foodscanner.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zybooks.foodscanner.data.Ingredients


@Composable
fun HomeScreen(modifier: Modifier = Modifier, onIngredientScreenClick: (String) -> Unit = { }, onCameraScreenClick: () -> Unit = { } ){

    Column(horizontalAlignment=Alignment.CenterHorizontally) {
        Text("How would you like to add ingredients?")
        Button(onClick = {onCameraScreenClick()}) {
            Text("Through Camera")
        }
        ImagePicker(onIngredientScreenClick)
        Button(onClick = {onIngredientScreenClick("")}, modifier = modifier.align(Alignment.CenterHorizontally)) {
            Text("Through Text")
        }
    }

}

@Composable
fun IngredientListScreen(
    scannedIngredients: String,
    modifier: Modifier = Modifier,
    viewModel : AddViewModel,
    onRecipeListNavigate: (String, String) -> Unit,
    onUpClick: () -> Unit = {  }
){


    fun handleSaveIngredients(){
        val newIngredient = Ingredients(viewModel.inputIngredientName, viewModel.quantityInput + " " + viewModel.units)
        viewModel.addIngredient(newIngredient)
        viewModel.setShowInput()
    }

    fun handleRouteToRecipe(ingredients: List<Ingredients>, mealType : String = viewModel.autoMealType.value) {

        var ingredientString = ""
        ingredients.forEach { ingredient -> ingredientString += "${ingredient.name}," }
        if (viewModel.ingredientsList.isNotEmpty()){
            onRecipeListNavigate(ingredientString, mealType)
        }
    }

    if (scannedIngredients != "" && !viewModel.addedScanIngredients) {
        val ingredients = scannedIngredients.split(",").map { it.trim() }
        ingredients.forEach { ingredient ->
            val ingredientCountPair = ingredient.split("=")
            val newIngredient = Ingredients(ingredientCountPair[0], ingredientCountPair[1])
            viewModel.addIngredient(newIngredient)
        }
        viewModel.addedScanIngredients = true
    }
    Column(modifier = modifier.fillMaxSize()) {
        RecipeTopBar(
            title = "Add Ingredients",
            modifier = modifier,
            canNavigateBack = true,
            onUpClick = {
                onUpClick() }
        )
        Column(modifier = modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally,  verticalArrangement = Arrangement.Center) {
            IngredientTable(modifier, viewModel)
            Spacer(Modifier.size(100.dp))
            if (!(viewModel.showInput || viewModel.showSelectMealType)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = { viewModel.setShowInput() }) {
                        Text("Add More")
                    }
                    Button(onClick = { viewModel.clearIngredients() }) {
                        Text("CLear All")
                    }
                    Button(onClick = { viewModel.showSelectMealType = true }) {
                        Text("Submit")
                    }
                }
            }
            else if(viewModel.showSelectMealType) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Select a Meal Type")
                    Button(onClick = { handleRouteToRecipe(viewModel.ingredientsList, viewModel.autoMealType.value) }) {
                        Text("Auto Meal Type: ${viewModel.autoMealType.value}")
                    }
                    Button(onClick = { handleRouteToRecipe(viewModel.ingredientsList, "breakfast") }) {
                        Text("Breakfast")
                    }
                    Button(onClick = { handleRouteToRecipe(viewModel.ingredientsList, "lunch") }) {
                        Text("Lunch")
                    }
                    Button(onClick = { handleRouteToRecipe(viewModel.ingredientsList, "lunch") }) {
                        Text("Snack")
                    }
                    Button(onClick = { handleRouteToRecipe(viewModel.ingredientsList, "dinner") }) {
                        Text("Dinner")
                    }
                    Button(onClick = { handleRouteToRecipe(viewModel.ingredientsList, "mid night snack") }) {
                        Text("Mid Night Snack")
                    }
                }
            }
            else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextField(
                        value = viewModel.inputIngredientName,
                        onValueChange = { name -> viewModel.updateName(name) },
                        label = { Text("Ingredient") })
                    TextField(
                        value = viewModel.quantityInput,
                        onValueChange = { quantity -> viewModel.updateQuantity(quantity) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("Amount") })
                    TextField(
                        value = viewModel.units,
                        onValueChange = { unit -> viewModel.updateUnits(unit) },
                        label = { Text("Units of Measurement") })
                    Button(onClick = { handleSaveIngredients() }) {
                        Text("Save")
                    }
                    Button(onClick = { viewModel.setShowInput() }) {
                        Text("Cancel")
                    }

                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientTable(modifier: Modifier, viewModel: AddViewModel){
    @Composable
    fun mapIngredients(ingredientList: List<Ingredients>){


        Log.i("test", ingredientList.toString())
        LazyColumn(
            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){ item {
            ingredientList.forEach {
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->
                        when (value) {
                            SwipeToDismissBoxValue.EndToStart -> {
                                viewModel.removeIngredient(it)
                                true
                            }

                            else -> false
                        }
                    }
                )


                SwipeToDismissBox(
                    state = dismissState,
                    content = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp)
                        ) {
                            Box(modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(text = it.name, fontWeight = FontWeight.W200)
                            }
                            Spacer(Modifier.size(20.dp))
                            Box(modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(text = it.amount, fontWeight = FontWeight.W200)
                            }
                            if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                                Box(
                                    modifier = Modifier.weight(0.5f).background(
                                        androidx.compose.material3.MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                                    ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                                        contentDescription = "Delete Ingredient",
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                        }
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

    Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally ){
        Row(modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            Text("Ingredient Name", fontWeight = FontWeight.Bold)
            Spacer(Modifier.size(20.dp))
            Text("Ingredient Amount", fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(10.dp))
        if (viewModel.ingredientsList.isEmpty()){
            Text("Let's add some ingredients!")
        }
        else{
            mapIngredients(viewModel.ingredientsList)
        }
    }
}

