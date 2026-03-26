package org.example.project.domain.usecase

import org.example.project.domain.ShoppingRepository
import org.example.project.domain.model.ShoppingItem

class RemoveShopListUseCase (
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(listId: Long) =
        repository.removeShopList(listId)
}