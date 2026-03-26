package org.example.project.data

import org.example.project.data.dto.SuccessResponse
import org.example.project.domain.model.ShoppingItem
import org.example.project.domain.model.ShoppingList
import org.example.project.domain.ShoppingRepository
import org.example.project.util.NetworkError
import org.example.project.util.Result
import org.example.project.util.map
import kotlin.collections.map

class ShoppingRepositoryImpl(
    private val apiService: ApiService
): ShoppingRepository {
    override suspend fun addToShopList(
        listId: Long,
        value: String,
        n: Int
    ): Result<SuccessResponse, NetworkError> {
        return apiService.addToShopList(listId, value, n)
    }

    override suspend fun updateItemShopList(
        itemId: Long,
        name: String,
        n: Int
    ): Result<SuccessResponse, NetworkError> {
        return apiService.updateItemShopList(itemId, name, n)
    }

    override suspend fun removeItemFromList(itemId: Long): Result<SuccessResponse, NetworkError> {
        return apiService.removeFromList(itemId)
    }

    override suspend fun crossItemOff(itemId: Long): Result<SuccessResponse, NetworkError> {
        return apiService.crossItemOff(itemId)
    }

    override suspend fun moveItem(
        items: List<ShoppingItem>,
        draggedIndex: Int,
        targetPosition: Int
    ) {
        val toIndex = if(targetPosition > draggedIndex) {
            targetPosition - 1
        } else {
            targetPosition
        }

        items.forEachIndexed { index, item ->
            val newOrderNumber = when {
                // сам перетаскиваемый элемент
                index == draggedIndex -> {
                    toIndex
                }

                // если перемещается наверх
                draggedIndex < toIndex &&
                        index in (draggedIndex + 1)..toIndex -> {
                    index - 1
                }

                // если перемещается вниз
                draggedIndex > toIndex &&
                        index in toIndex until draggedIndex -> {
                    index + 1
                }

                else -> index
            }
            apiService.moveItem(item.id, newOrderNumber.toLong())
        }
    }

    override suspend fun removeShopList(listId: Long): Result<SuccessResponse, NetworkError> {
        return apiService.removeShopList(listId)
    }

    override suspend fun createShopList(
        key: String,
        name: String
    ): Result<Long, NetworkError> {
        return apiService.createShopList(key, name).map { response ->
            response.listId
        }
    }

    override suspend fun registerGetKey(): Result<String, NetworkError> {
        return apiService.register().map { response ->
            response.key
        }
    }

    override suspend fun auth(key: String): Result<SuccessResponse, NetworkError> {
        return apiService.auth(key)
    }

    override suspend fun getAllShopLists(key: String): Result<List<ShoppingList>, NetworkError> {
        return apiService.getAllShopLists(key).map { response ->
            response.lists.map { it.toShoppingList() }
        }
    }

    override suspend fun getShopList(listId: Long): Result<List<ShoppingItem>, NetworkError> {
        return apiService.getShopList(listId).map { response ->
            response.shopList.map { it.toShoppingItem() }
        }
    }

}