package com.zybooks.foodscanner.data

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,
    val usedIngredientCount: Int,
    val missedIngredientCount: Int,
    val missedIngredients: List<Ingredient>,
    val usedIngredients: List<Ingredient>,
    val unusedIngredients: List<Ingredient>,
    val likes: Int
) {
    data class Ingredient(
        val id: Int,
        val amount: Double,
        val unit: String,
        val unitLong: String,
        val unitShort: String,
        val aisle: String,
        val name: String,
        val original: String,
        val originalName: String,
        val meta: List<String>,
        val extendedName: String?,
        val image: String
    )
}

typealias RecipeList = List<Recipe>