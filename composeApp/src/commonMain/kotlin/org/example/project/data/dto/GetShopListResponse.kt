package org.example.project.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.project.domain.model.ShoppingItem

@Serializable
data class GetShopListResponse(
    @SerialName("item_list")
    val shopList: List<ShoppingItemDto>,
    val success: Boolean
)

@Serializable
data class ShoppingItemDto(
    val name: String,
    @SerialName("is_crossed")
    val isCrossed: Boolean,
    val id: Long,
    val n: Int
) {
    fun toShoppingItem(): ShoppingItem {
        return ShoppingItem(
            name = name,
            isCrossed = isCrossed,
            id = id,
            n = n
        )
    }
}