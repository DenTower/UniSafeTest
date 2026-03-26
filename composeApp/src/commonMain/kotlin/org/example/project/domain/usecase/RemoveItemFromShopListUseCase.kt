package org.example.project.domain.usecase

import org.example.project.domain.ShoppingRepository

class RemoveItemFromShopListUseCase (
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(itemId: Long) =
        repository.removeItemFromList(itemId)
}