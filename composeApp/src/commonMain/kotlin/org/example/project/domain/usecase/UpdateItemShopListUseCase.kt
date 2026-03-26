package org.example.project.domain.usecase

import org.example.project.domain.ShoppingRepository

class UpdateItemShopListUseCase(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(itemId: Long, name: String, n: Int) =
        repository.updateItemShopList(itemId, name, n)
}