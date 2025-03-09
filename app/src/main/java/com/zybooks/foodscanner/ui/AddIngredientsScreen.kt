package com.zybooks.foodscanner.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zybooks.foodscanner.R
import com.zybooks.foodscanner.data.Ingredients
import com.zybooks.foodscanner.data.RecipeAPI
import com.zybooks.foodscanner.data.RecipeAPIService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//sealed class Routes{
//
//    @Serializable
//    data object HomeScreen
//
//    @Serializable
//    data object IngredientList
//
//    @Serializable
//    data object RecipeList
//}
//
//@Composable
//fun MealApp(modifier: Modifier){
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = Routes.HomeScreen)  {
//        composable<Routes.HomeScreen> {
//            HomeScreen(modifier, onIngredientScreenClick = {navController.navigate(Routes.IngredientList)})
//        }
//        composable<Routes.IngredientList> {
//            IngredientListScreen(modifier, AddViewModel(), onUpClick = {navController.navigateUp()}, onRecipeListNavigate = {navController.navigate(Routes.RecipeList)})
//        }
//        composable<Routes.RecipeList> {
//
//        }
//
//    }
//}

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onIngredientScreenClick: () -> Unit = { } ){

    Column(horizontalAlignment=Alignment.CenterHorizontally) {
        Text("How would you like to add ingredients?")
        Button(onClick = {}) {
            Text("Through Camera")
        }
        Button(onClick = {onIngredientScreenClick()}, modifier = modifier.align(Alignment.CenterHorizontally)) {
            Text("Through Text")
        }
    }

}

@Composable
fun IngredientListScreen(modifier: Modifier = Modifier, viewModel: AddViewModel, onRecipeListNavigate: () -> Unit = { }){

    val couroutine = rememberCoroutineScope()
    val apiKey = stringResource(R.string.recipe_api_key)

    val recipeAPIService: RecipeAPI by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/")
            .build()
        retrofit.create(RecipeAPI::class.java)
    }

    val recipeAPI = RecipeAPIService(recipeAPIService)

    fun handleSaveIngredients(){
        val newIngredient: Ingredients = Ingredients(viewModel.inputIngredientName, viewModel.quantityInput + " " + viewModel.units)
        viewModel.addIngredient(newIngredient)
        viewModel.setShowInput()
    }

    fun handleRouteToRecipe(ingredients: MutableList<Ingredients>){

        var ingredientString = ""
        ingredients.forEach { ingredient -> ingredientString += "${ingredient.name}," }
        if (viewModel.ingredientsList.isNotEmpty()){
            couroutine.launch { recipeAPI.getRecipes(apiKey, ingredientString) }
            onRecipeListNavigate()
        }
    }


    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        IngredientTable(modifier, viewModel)
        Spacer(Modifier.size(100.dp))
        if (!viewModel.showInput){
            Column (horizontalAlignment = Alignment.CenterHorizontally ){
                Button(onClick = {viewModel.setShowInput()}) {
                    Text("Add More")
                }
                Button(onClick = {handleRouteToRecipe(viewModel.ingredientsList)}) {
                    Text("Submit")
                }
            }
        }
        else{
            Column (horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TextField(value = viewModel.inputIngredientName, onValueChange = {name -> viewModel.updateName(name)}, label = { Text("Ingredient")})
                TextField(value = viewModel.quantityInput, onValueChange = {quantity -> viewModel.updateQuantity(quantity)}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), label = { Text("Amount")})
                TextField(value = viewModel.units, onValueChange = {unit -> viewModel.updateUnits(unit)}, label = { Text("Units of Measurement")})
                Button(onClick = {handleSaveIngredients()}) {
                    Text("Save")
                }
                Button(onClick = {viewModel.setShowInput()}) {
                    Text("Cancel")
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
        ingredientList.forEach { Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp)) {
            Text(text=it.name, fontWeight = FontWeight.W200)
            Spacer(Modifier.size(20.dp))
            Text(text=it.amount, fontWeight = FontWeight.W200)
        } }

    }

    Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally ){
        Row(modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            Text("Ingredient Name")
            Spacer(Modifier.size(20.dp))
            Text("Ingredient Amount")
        }
        if (viewModel.ingredientsList.isEmpty()){
            Text("Let's add some ingredients!")
        }
        else{
            mapIngredients(viewModel.ingredientsList)
        }
    }
}

