package com.zybooks.foodscanner.ui

import androidx.lifecycle.ViewModel
import com.zybooks.foodscanner.data.Recipe
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeViewModel: ViewModel() {
    val recipes = mutableListOf<Recipe>()
    private val _loading = MutableStateFlow(false)
    val loadingStatus : StateFlow<Boolean> = _loading.asStateFlow()
    fun addRecipe(recipe: Recipe){
        recipes.add(recipe)
    }

    fun setRecipes(recipeList: List<Recipe>){
        recipes.clear()
        recipes.addAll(recipeList)
    }

    fun setLoadingStatus(status: Boolean){
        _loading.value = status
    }
}