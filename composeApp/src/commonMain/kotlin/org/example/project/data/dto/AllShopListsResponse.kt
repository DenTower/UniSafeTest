package org.example.project.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.project.domain.model.ShoppingList
import org.example.project.util.formatTime

@Serializable
data class AllShopListsResponse(
    @SerialName("shop_list")
    val lists: List<ShoppingListDto>,
    val success: Boolean
)

@Serializable
data class ShoppingListDto(
    val created: String,
    val name: String,
    val id: Long
) {
    fun toShoppingList(): ShoppingList {
        return ShoppingList(
            created = formatTime(created),
            name = name,
            id = id,
            items = emptyList()
        )
    }
}