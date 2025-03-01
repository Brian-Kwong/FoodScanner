package com.zybooks.foodscanner.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel

val availableQuantitation = listOf("count","g", "kg", "ml", "l", "oz", "lb")

@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    numberOnly : Boolean = false,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if (numberOnly) KeyboardType.Number else KeyboardType.Text
        ),
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(
    title: String,
    values: List<String>,
    selectedIndex: Int,
    label: String,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    var expanded by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = 20.sp
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            TextField(
                label = { Text(label) },
                value = values[selectedIndex],
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = modifier
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                values.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            expanded = false
                            onItemClick(index)
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputTable(modifier: Modifier = Modifier, viewModel: InputViewModel = viewModel()) {

    Column(modifier = modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
    // Table that displays all inputted ingredients
        Box(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
            ) {
                stickyHeader {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ingredient",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(120.dp),
                            style = androidx.compose.ui.text.TextStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        )

                        Text(
                            text = "Quantity",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(70.dp),
                            style = androidx.compose.ui.text.TextStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        )

                        Text(
                            text = "Unit",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(90.dp),
                            style = androidx.compose.ui.text.TextStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        )
                    }
                }
                items(viewModel.inputtedIngredients.size) { index ->
                    val ingredient = viewModel.inputtedIngredients[index]

                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                            Text(
                                text = ingredient.foodName,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(120.dp)
                            )

                            Text(
                                text = ingredient.quantity,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(70.dp)
                            )

                            Text(
                                text = ingredient.unit,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(90.dp)
                            )
                        }
                }
            }
        }
    Column(modifier = modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        TextInput(value = viewModel.foodName.value, onValueChange = {
            viewModel.foodName.value = it
        }, label = "Enter the ingredient name")
        TextInput(value = viewModel.quantity.value, onValueChange = {
            viewModel.quantity.value = it
        }, label = "Enter the quantity", numberOnly = true)
        // Drop down for quantity unit
        Dropdown(
            title = "",
            values = availableQuantitation,
            selectedIndex = viewModel.selectedUnit.intValue,
            label = "Select an unit",
            onItemClick = {
                // Update the selected index
                viewModel.selectedUnit.intValue = it
            },
            modifier = Modifier
        )
        Button(modifier = modifier.align(Alignment.CenterHorizontally).fillMaxWidth().padding(30.dp), onClick = {
            viewModel.addIngredient()
        }) {
            Text("Add")
        }
    }}
}
