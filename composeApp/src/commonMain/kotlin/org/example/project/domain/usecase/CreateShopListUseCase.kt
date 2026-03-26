package org.example.project.domain.usecase

import org.example.project.domain.ShoppingRepository

class CreateShopListUseCase(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(key: String, name: String) =
        repository.createShopList(key, name)
}