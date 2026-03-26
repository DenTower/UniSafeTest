package org.example.project.domain.model

data class ShoppingList(
    val created: String,
    val name: String,
    val id: Long,
    val items: List<ShoppingItem>
)
