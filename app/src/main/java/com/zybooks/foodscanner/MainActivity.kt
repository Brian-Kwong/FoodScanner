package com.zybooks.foodscanner

import android.util.Base64
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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zybooks.foodscanner.ui.AddViewModel
import com.zybooks.foodscanner.ui.CameraScreen
import com.zybooks.foodscanner.ui.CameraScreenViewModel
import com.zybooks.foodscanner.ui.DetailedRecipeScreen
import com.zybooks.foodscanner.ui.HomeScreen
import com.zybooks.foodscanner.ui.IngredientListScreen
import com.zybooks.foodscanner.ui.RecipeTableScreen
import com.zybooks.foodscanner.ui.RecipeViewModel
import com.zybooks.foodscanner.ui.theme.FoodScannerTheme

object APIKeyLibrary {
    // Reads in the API Key from a compiled C++ library
    init {
        System.loadLibrary("api-keys")
    }

    external fun getAPIKey(): String
}

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

    val cameraScreenViewModel = CameraScreenViewModel()

    val addViewModel : AddViewModel = viewModel(
        viewModelStoreOwner = LocalViewModelStoreOwner.current!!,
    )

    val recipeViewModel : RecipeViewModel = viewModel(
        viewModelStoreOwner = LocalViewModelStoreOwner.current!!,
    )


    recipeViewModel.setAPIKey(  Base64.decode(APIKeyLibrary.getAPIKey(), Base64.DEFAULT).toString(Charsets.UTF_8))



    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                   HomeScreen(modifier, onIngredientScreenClick = {
                       addViewModel.addedScanIngredients = it.isEmpty()
                       navController.navigate("input/${it}")}, onCameraScreenClick = {navController.navigate("take-picture-camera")})
                }
            }
        }
        composable("input/{scannedIngredients}", arguments = listOf(navArgument("scannedIngredients") { defaultValue = "" } )) { backStackEntry ->
            val scannedIngredients = remember { backStackEntry.arguments?.getString("scannedIngredients") ?: "" }
            Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    IngredientListScreen(scannedIngredients, Modifier,addViewModel, onRecipeListNavigate = { ingredients, mealType ->
                        recipeViewModel.clearRecipes()
                        navController.navigate("recipe-list/${ingredients}/${mealType.replace(" ", "_")}")
                    }, onUpClick = {
                        if (addViewModel.showSelectMealType) {
                            addViewModel.showSelectMealType = false
                        } else {
                            navController.navigateUp()
                        }})
                }
            }
        }
        composable("recipe-list/{ingredients}/{mealType}", arguments = listOf(navArgument("ingredients") { defaultValue = "" },navArgument("mealType") { defaultValue = "" })) { backStackEntry ->
            val ingredients = remember { backStackEntry.arguments?.getString("ingredients") ?: "" }
            val mealType = remember { backStackEntry.arguments?.getString("mealType") ?: addViewModel.autoMealType.value }
            Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    RecipeTableScreen(ingredients, mealType.replace("_", " "), Modifier, recipeViewModel , onRecipeClick = {
                        navController.navigate("detailed-recipe")
                    }, onUpClick = {
                        navController.navigateUp()})
                }
            }
        }
        composable("detailed-recipe") {
            Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    DetailedRecipeScreen(detailedRecipeViewModel = recipeViewModel, Modifier, UpClick = {
                        navController.navigateUp()})
                }
            }
        }
        composable("take-picture-camera") {
            Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CameraScreen(cameraScreenViewModel = cameraScreenViewModel, navigateToIngredients = {
                        addViewModel.addedScanIngredients = it.isEmpty()
                        navController.navigate("input/${it}")
                    })
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
