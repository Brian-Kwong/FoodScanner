package com.zybooks.foodscanner.data

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zybooks.foodscanner.ui.RecipeViewModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

import retrofit2.http.Query


interface RecipeAPI{

    @GET("findByIngredients?")
    suspend fun getRecipes(
        @Header("x-rapidapi-key") apiKey: String,
        @Query("ingredients") ingredients: String): RecipeList

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

    suspend fun getRecipes(apiKey: String, ingredient: String): RecipeList {
        Log.i("RecipeAPIService", "getRecipes")
        Log.i("RecipeAPIService", "apiKey: $apiKey")
        Log.i("RecipeAPIService", "ingredient: $ingredient")
        Log.i("Response", service.getRecipes(apiKey, ingredient).toString())
        val recipeList  = service.getRecipes(apiKey, ingredient)
        return recipeList
    }

    suspend fun getRecipeInformationBulk(apiKey: String, idList: List<String>): RecipeDetailsList {

        // Reduce string list to a single string using URL encoding
        val ids = idList.joinToString(separator = "%") { it }

        Log.i("RecipeAPIService", "getRecipeInformation")
        Log.i("RecipeAPIService", "apiKey: $apiKey")
        Log.i("RecipeAPIService", "ids: $ids")
        Log.i("Response", service.getRecipeInformationBulk(apiKey, ids).toString())
        val recipeDetailsList = service.getRecipeInformationBulk(apiKey, ids)
        return recipeDetailsList
    }

    suspend fun getRecipeInformation(apiKey: String, id: String): RecipeDetails {
        // Reduce string list to a single string using URL encoding
        Log.i("RecipeAPIService", "getRecipeInformation")
        Log.i("RecipeAPIService", "apiKey: $apiKey")
        Log.i("RecipeAPIService", "ids: $id")
        Log.i("Response", service.getRecipeInformation(apiKey, id).toString())
        val recipeDetails = service.getRecipeInformation(apiKey, id)
        return recipeDetails

    }
}