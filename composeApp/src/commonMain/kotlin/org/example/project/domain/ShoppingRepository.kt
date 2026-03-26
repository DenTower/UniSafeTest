package org.example.project.domain

import org.example.project.data.dto.SuccessResponse
import org.example.project.domain.model.ShoppingItem
import org.example.project.domain.model.ShoppingList
import org.example.project.util.NetworkError
import org.example.project.util.Result

interface ShoppingRepository {

    suspend fun addToShopList(
        listId: Long,
        value: String,
        n: Int
    ): Result<SuccessResponse, NetworkError>

    suspend fun updateItemShopList(
        itemId: Long,
        name: String,
        n: Int
    ): Result<SuccessResponse, NetworkError>

    suspend fun removeItemFromList(
        itemId: Long
    ): Result<SuccessResponse, NetworkError>

    suspend fun crossItemOff(
        itemId: Long
    ): Result<SuccessResponse, NetworkError>

    suspend fun moveItem(
        items:  List<ShoppingItem>, draggedIndex: Int, targetPosition: Int
    )

    suspend fun removeShopList(
        listId: Long
    ): Result<SuccessResponse, NetworkError>

    suspend fun createShopList(
        key: String,
        name: String
    ): Result<Long, NetworkError>

    suspend fun registerGetKey(): Result<String, NetworkError>

    suspend fun auth(
        key: String
    ): Result<SuccessResponse, NetworkError>

    suspend fun getAllShopLists(
        key: String
    ): Result<List<ShoppingList>, NetworkError>

    suspend fun getShopList(
        listId: Long
    ): Result<List<ShoppingItem>, NetworkError>
}