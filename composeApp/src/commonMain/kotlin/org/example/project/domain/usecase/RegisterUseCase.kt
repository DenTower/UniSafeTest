package org.example.project.domain.usecase

import org.example.project.domain.ShoppingRepository

class RegisterUseCase(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke() =
        repository.registerGetKey()
}