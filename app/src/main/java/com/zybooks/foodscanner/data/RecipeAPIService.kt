package com.zybooks.foodscanner.data

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zybooks.foodscanner.ui.RecipeViewModel
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
        @Query("maxReadyTime") maxReadyTime: Int = 45,
        @Query("ignorePantry") ignorePantry: Boolean = true,
        @Query("offset") offset: Int = 0,
        @Query("number") number: Int = 10
    ): RecipeResponse

    @GET("informationBulk?")
    suspend fun getRecipeInformationBulk(
        @Header("x-rapidapi-key") apiKey : String,
        @Query("ids") ids: String): RecipeDetailsList

    @GET("{id}/information")
    suspend fun getRecipeInformation(
        @Header("x-rapidapi-key") apiKey : String,
        @Path("id") id: String): RecipeDetails

}

class RecipeAPIService (private val service: RecipeAPI) {

    suspend fun getRecipes(apiKey: String, ingredient: String, mealType : String): RecipeList {
        Log.i("RecipeAPIService", "getRecipes")
        Log.i("RecipeAPIService", "apiKey: $apiKey")
        Log.i("RecipeAPIService", "ingredient: $ingredient")

        val recipeList  = service.getRecipes(apiKey, mealType, ingredient.dropLast(1)).results
        Log.i("RecipeAPIService", "recipeList: $recipeList")
        return recipeList
    }

    suspend fun getRecipeInformationBulk(apiKey: String, idList: List<String>): RecipeDetailsList {

        // Reduce string list to a single string using URL encoding
        val ids = idList.joinToString(separator = "%") { it }

        Log.i("RecipeAPIService", "getRecipeInformation")
        Log.i("RecipeAPIService", "apiKey: $apiKey")
        Log.i("RecipeAPIService", "ids: $ids")
        val recipeDetailsList = service.getRecipeInformationBulk(apiKey, ids)
        return recipeDetailsList
    }

    suspend fun getRecipeInformation(apiKey: String, id: String): RecipeDetails {
        // Reduce string list to a single string using URL encoding
        Log.i("RecipeAPIService", "getRecipeInformation")
        Log.i("RecipeAPIService", "apiKey: $apiKey")
        Log.i("RecipeAPIService", "ids: $id")
        val recipeDetails = service.getRecipeInformation(apiKey, id)
        return recipeDetails

    }
}