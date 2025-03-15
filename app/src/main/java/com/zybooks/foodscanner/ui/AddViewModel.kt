package com.zybooks.foodscanner.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zybooks.foodscanner.data.Ingredients


class AddViewModel: ViewModel() {
    private val _ingredientsList =  mutableStateListOf<Ingredients>()
    val ingredientsList: List<Ingredients>
        get() = _ingredientsList
    var inputIngredientName by mutableStateOf("")
    var quantityInput by mutableStateOf("")
    var units by mutableStateOf("")
    var showInput by mutableStateOf(false)
    var addedScanIngredients by mutableStateOf(false)
    var showSelectMealType by  mutableStateOf(false)

    // Depending time of day auto set mealType to breakfast, lunch, or dinner
    val autoMealType = mutableStateOf(
        when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
            in 2..5 -> "Mid Night Snack"
            in 6..10 -> "Breakfast"
            in 11..14 -> "Lunch"
            in 15..17 -> "Snack"
            in 18..22 -> "Dinner"
            else -> "Late Night Snack"
        }
    )

    fun setMealType(mealType: String){
        autoMealType.value = mealType
    }

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
        _ingredientsList.add(ingredients)
    }

    fun removeIngredient(ingredients: Ingredients){
        _ingredientsList.remove(ingredients)
    }

    fun clearIngredients(){
        _ingredientsList.clear()
    }
}