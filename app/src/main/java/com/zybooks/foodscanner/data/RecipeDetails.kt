package com.zybooks.foodscanner.data


data class RecipeDetails(
    val id : Int,
    val image : String,
    val imageType : String,
    val title : String,
    val readyInMinutes : Int,
    val servings : Int,
    val sourceUrl : String,
    val vegetarian : Boolean,
    val vegan : Boolean,
    val glutenFree : Boolean,
    val dairyFree : Boolean,
    val veryHealthy : Boolean,
    val cheap : Boolean,
    val veryPopular : Boolean,
    val sustainable : Boolean,
    val lowFodmap : Boolean,
    val weightWatcherSmartPoints : Int,
    val gaps : String,
    val preparationMinutes : Int,
    val cookingMinutes : Int,
    val aggregateLikes : Int,
    val healthScore : Int,
    val creditsText : String,
    val license : String,
    val sourceName : String,
    val pricePerServing : Double,
    val extendedIngredients : List<IngredientsDetails>,
    val summary : String,
    val cuisines : List<String>,
    val dishTypes : List<String>,
    val occasions : List<String>,
    val instructions : String,
    val analyzedInstructions : List<AnalyzedInstructions>,
    val originalId : Int,
    val spoonacularScore : Double
){
    data class IngredientsDetails(
        val id : Int,
        val aisle : String,
        val image : String,
        val consistency : String,
        val name : String,
        val nameClean : String,
        val original : String,
        val originalName : String,
        val amount : Double,
        val unit : String
    )


    data class AnalyzedInstructions(
        val name : String,
        val steps : List<Steps>
    )

    data class Steps(
        val number : Int,
        val step : String,
        val ingredients : List<Ingredients>,
        val equipment : List<Equipment>
    )

    data class Ingredients(
        val id : Int,
        val name : String,
        val localizedName : String,
        val image : String
    )

    data class Equipment(
        val id : Int,
        val name : String,
        val localizedName : String,
        val image : String
    )
}

