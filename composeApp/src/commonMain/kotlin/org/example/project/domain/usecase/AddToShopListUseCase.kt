package org.example.project.domain.usecase

import org.example.project.domain.ShoppingRepository

class AddToShopListUseCase (
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(listId: Long, name: String, n: Int) =
        repository.addToShopList(listId, name, n)
}