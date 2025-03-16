package com.zybooks.foodscanner.data

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface RecipeAPI{

    @GET("complexSearch?")
    suspend fun getRecipes(
        @Header("x-rapidapi-key") apiKey: String,
        @Query("query") foodType: String,
        @Query("includeIngredients") ingredient: String,
        @Query("sort") sort: String = "max-used-ingredients",
        @Query("instructionsRequired") instructionsRequired: Boolean = true,
        @Query("fillIngredients") fillIngredients: Boolean = false,
        @Query("addRecipeInformation") addRecipeInformation: Boolean = false,
        @Query("addRecipeInstructions") addRecipeInstructions: Boolean = false,
        @Query("addRecipeNutrition") addRecipeNutrition: Boolean = false,
        @Query("maxReadyTime") maxReadyTime: Int = 120,
        @Query("ignorePantry") ignorePantry: Boolean = true,
        @Query("offset") offset: Int = 0,
        @Query("number") number: Int = 10
    ): RecipeResponse

    @GET("{id}/information")
    suspend fun getRecipeInformation(
        @Header("x-rapidapi-key") apiKey : String,
        @Path("id") id: String): RecipeDetails

}

class RecipeAPIService (private val service: RecipeAPI) {

    suspend fun getRecipes(apiKey: String, ingredient: String, mealType : String): RecipeList {

        val recipeList  = service.getRecipes(apiKey, mealType, ingredient.dropLast(1)).results
        return recipeList
    }

    suspend fun getRecipeInformation(apiKey: String, id: String): RecipeDetails {
        // Reduce string list to a single string using URL encoding
        val recipeDetails = service.getRecipeInformation(apiKey, id)
        return recipeDetails

    }
}