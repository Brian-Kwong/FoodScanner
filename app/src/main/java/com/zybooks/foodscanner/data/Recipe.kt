package com.zybooks.foodscanner.data


data class RecipeResponse(
    val results: List<Recipe>,
    val offset: Int,
    val number:Int,
    val totalResults: Int
) {
}



data class  Recipe(
    val id: Int,
    val usedIngredientCount: Int,
    val missedIngredientCount: Int,
    val likes: Int,
    val title: String,
    val image: String,
    val imageType: String,)
{}

typealias RecipeList = List<Recipe>