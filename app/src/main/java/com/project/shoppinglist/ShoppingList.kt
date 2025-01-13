package com.project.shoppinglist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id: Int, var name: String, var quantity: Int, var isEditing: Boolean = false)


@Composable
fun ShoppingList() {
    var sItems = remember { mutableStateOf(listOf<ShoppingItem>()) }
    var itemName by remember { mutableStateOf("")}
    var itemQuantity by remember { mutableStateOf("")}
    var showDialog by remember { mutableStateOf(false)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text("Add Item")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            items(sItems.value) { item ->
                if (item.isEditing) {
                    ShoppingItemEditor(
                        item = item,
                        onEditComplete = { editedName, editedQuantity ->
                            sItems.value = sItems.value.map { it.copy(isEditing = false) }
                            val editedItem = sItems.value.find { it.id == item.id }
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editedQuantity
                            }
                        }
                    )
                } else {
                    ShoppingListItem(
                        item = item,
                        onEditClick = {
                            sItems.value = sItems.value.map { it.copy(isEditing = it.id == item.id) }
                        },
                        onDeleteClick = { sItems.value = sItems.value - item }
                    )
                }
            }
        }

    }

    if (showDialog) {
        itemQuantity = "1"
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Shopping Item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {
                            itemQuantity = it

                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (itemName.isNotBlank()) {
                                val newItem = ShoppingItem(
                                    id = sItems.value.size + 1,
                                    name = itemName,
                                    quantity = itemQuantity.toInt()
                                )
                                sItems.value = sItems.value + newItem
                                showDialog = false
                                itemName = ""
                            }
                        }
                    ) {
                        Text("Add")
                    }
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}