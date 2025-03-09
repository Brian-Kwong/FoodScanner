package com.zybooks.foodscanner.data

import android.util.Log
import retrofit2.http.GET
import retrofit2.http.Header

import retrofit2.http.Query


interface RecipeAPI{

    @GET("findByIngredients?")
    suspend fun getRecipes(
        @Header("x-rapidapi-key") apiKey: String,
        @Query("ingredients") ingredients: String): RecipeList

}

class RecipeAPIService (val service: RecipeAPI){

    suspend fun getRecipes(apiKey: String, ingredient: String): RecipeList {
        return service.getRecipes(apiKey, ingredient)
    }

}