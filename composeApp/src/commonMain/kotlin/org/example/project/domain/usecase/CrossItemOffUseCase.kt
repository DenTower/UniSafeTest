package org.example.project.domain.usecase

import org.example.project.domain.ShoppingRepository

class CrossItemOffUseCase(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(itemId: Long) =
        repository.crossItemOff(itemId)
}