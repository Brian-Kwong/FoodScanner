package com.zybooks.foodscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zybooks.foodscanner.ui.AddViewModel
import com.zybooks.foodscanner.ui.DetailedRecipeScreen
import com.zybooks.foodscanner.ui.HomeScreen
import com.zybooks.foodscanner.ui.IngredientListScreen
import com.zybooks.foodscanner.ui.RecipeTableScreen
import com.zybooks.foodscanner.ui.RecipeViewModel
import com.zybooks.foodscanner.ui.theme.FoodScannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodScannerTheme {
                App(Modifier)
            }
        }
    }
}

@Composable
fun App(modifier: Modifier) {
    val navController = rememberNavController()

    // Create s shared recipe view model to store recipes
    // Made by nav host to be able to pass it to the screens
    val recipeViewModel : RecipeViewModel = viewModel(
        viewModelStoreOwner = LocalViewModelStoreOwner.current!!,
    )
    recipeViewModel.setAPIKey(stringResource(R.string.api_key))



    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                   HomeScreen(modifier, onIngredientScreenClick = {navController.navigate("input")})
                }
            }
        }
        composable("input") {
            Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    IngredientListScreen(Modifier, viewModel = AddViewModel(),recipeViewModel, onRecipeListNavigate = {navController.navigate("recipe-list")}, onUpClick = {navController.navigateUp()})
                }
            }
        }
        composable("recipe-list") {
            Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    RecipeTableScreen(Modifier, recipeViewModel , onRecipeClick = {
                        navController.navigate("detailed-recipe")
                    }, onUpClick = {navController.navigateUp()})
                }
            }
        }
        composable("detailed-recipe") {
            Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    DetailedRecipeScreen(detailedRecipeViewModel = recipeViewModel, Modifier)
                }
            }
        }

    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FoodScannerTheme {
        Greeting("Android")
    }
}
