package com.zybooks.foodscanner.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zybooks.foodscanner.data.Ingredients


class AddViewModel: ViewModel() {
    val ingredientsList = mutableListOf<Ingredients>()
    var inputIngredientName by mutableStateOf("")
    var quantityInput by mutableStateOf("")
    var units by mutableStateOf("")
    var showInput by mutableStateOf(false)

    fun updateName(newIngredientName: String){
        inputIngredientName = newIngredientName
    }

    fun updateQuantity(newQuantity: String){
        quantityInput = newQuantity
    }

    fun updateUnits(newUnits: String){
        units = newUnits
    }

    fun setShowInput(){
        showInput = !showInput
    }

    fun addIngredient(ingredients: Ingredients){
        ingredientsList.add(ingredients)
    }

}