package com.zybooks.foodscanner.ui

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

}