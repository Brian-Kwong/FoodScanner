package com.zybooks.foodscanner.ui

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.zybooks.foodscanner.IngredientEntry

class InputViewModel : ViewModel() {

    val inputtedIngredients = mutableListOf<IngredientEntry>();
    val foodName = mutableStateOf("")
    val quantity = mutableStateOf("")
    val selectedUnit =  mutableIntStateOf(0)


    fun addIngredient() {
        inputtedIngredients.add(IngredientEntry(foodName.value, quantity.value, availableQuantitation[selectedUnit.value]))
        foodName.value = ""
        quantity.value = ""
        selectedUnit.intValue = 0
        Log.d("InputViewModel", "addIngredient: $inputtedIngredients")
    }
}