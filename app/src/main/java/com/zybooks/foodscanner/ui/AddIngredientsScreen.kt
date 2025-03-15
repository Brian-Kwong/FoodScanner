package com.zybooks.foodscanner.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
    onRecipeListNavigate: (String) -> Unit = { },
    onUpClick: () -> Unit = {  }
){


    fun handleSaveIngredients(){
        val newIngredient = Ingredients(viewModel.inputIngredientName, viewModel.quantityInput + " " + viewModel.units)
        viewModel.addIngredient(newIngredient)
        viewModel.setShowInput()
    }

    fun handleRouteToRecipe(ingredients: List<Ingredients>){

        var ingredientString = ""
        ingredients.forEach { ingredient -> ingredientString += "${ingredient.name}," }
        if (viewModel.ingredientsList.isNotEmpty()){
            onRecipeListNavigate(ingredientString)
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
            if (!viewModel.showInput) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = { viewModel.setShowInput() }) {
                        Text("Add More")
                    }
                    Button(onClick = { viewModel.clearIngredients() }) {
                        Text("CLear All")
                    }
                    Button(onClick = { handleRouteToRecipe(viewModel.ingredientsList) }) {
                        Text("Submit")
                    }
                }
            } else {
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

@Composable
fun IngredientTable(modifier: Modifier, viewModel: AddViewModel){
    @Composable
    fun mapIngredients(ingredientList: List<Ingredients>){
        Log.i("test", ingredientList.toString())
        Column ( modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.9f), horizontalAlignment = Alignment.CenterHorizontally,) {
            ingredientList.forEach {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp)
                ) {
                    Box(modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(text = it.name, fontWeight = FontWeight.W200)
                    }
                    Spacer(Modifier.size(20.dp))
                    Box(modifier.weight(1f) , contentAlignment = Alignment.Center) {
                        Text(text = it.amount, fontWeight = FontWeight.W200)
                    }
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

