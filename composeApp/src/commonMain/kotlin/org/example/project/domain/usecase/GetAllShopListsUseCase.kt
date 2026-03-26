package org.example.project.domain.usecase

import org.example.project.domain.ShoppingRepository

class GetAllShopListsUseCase(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(key: String) =
        repository.getAllShopLists(key)
}