package com.zybooks.foodscanner.ui

import androidx.lifecycle.ViewModel
import com.zybooks.foodscanner.data.Recipe

class RecipeViewModel: ViewModel() {
    val recipes = mutableListOf<Recipe>()
    fun addRecipe(recipe: Recipe){
        recipes.add(recipe)
    }
}