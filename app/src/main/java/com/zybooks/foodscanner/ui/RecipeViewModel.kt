package com.zybooks.foodscanner.ui

import androidx.lifecycle.ViewModel
import com.zybooks.foodscanner.data.Recipe
import com.zybooks.foodscanner.data.RecipeAPI
import com.zybooks.foodscanner.data.RecipeAPIService
import com.zybooks.foodscanner.data.RecipeDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipeViewModel : ViewModel() {
    private lateinit var apiKey : String
    val recipes = mutableListOf<Recipe>()
    private val _selectedRecipe = MutableStateFlow<RecipeDetails?>(null)
    val selectedRecipeDetails : StateFlow<RecipeDetails?> = _selectedRecipe.asStateFlow()
    private val _loading = MutableStateFlow(false)
    private val _noResults = MutableStateFlow(false)
    val loadingStatus : StateFlow<Boolean> = _loading.asStateFlow()
    val noResultsStatus : StateFlow<Boolean> = _noResults.asStateFlow()
    private val coroutine =   CoroutineScope(Dispatchers.IO)

    private val recipeAPIService: RecipeAPI by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/")
            .build()
        retrofit.create(RecipeAPI::class.java)
    }

    private val recipeAPI = RecipeAPIService(recipeAPIService)


    fun setAPIKey(apiKey : String){
        this.apiKey = apiKey
    }

    fun clearRecipes(){
        recipes.clear()
    }

    fun fetchRecipeInformation(ingredientString : String, mealType : String){
        coroutine.launch {
            _loading.value = true
            val results = recipeAPI.getRecipes(apiKey, ingredientString,mealType)
            recipes.clear()
            recipes.addAll(results)
            _noResults.value = results.isEmpty()
            _loading.value = false
        }
    }

    fun fetchSelectedRecipe(recipeId: String){
        _loading.value = true
        coroutine.launch {
            val recipeDetails = recipeAPI.getRecipeInformation(apiKey, recipeId)
            _selectedRecipe.value = recipeDetails
            _loading.value = false
        }
    }

    fun setNoResultsStatus(status: Boolean) {
        _noResults.value = status
    }
}