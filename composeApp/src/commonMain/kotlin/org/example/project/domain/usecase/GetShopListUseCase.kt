package org.example.project.domain.usecase

import org.example.project.domain.ShoppingRepository

class GetShopListUseCase(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(listId: Long) =
        repository.getShopList(listId)
}