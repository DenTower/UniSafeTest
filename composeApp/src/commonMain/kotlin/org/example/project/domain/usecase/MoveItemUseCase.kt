package org.example.project.domain.usecase

import org.example.project.domain.ShoppingRepository
import org.example.project.domain.model.ShoppingItem

class MoveItemUseCase(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(items:  List<ShoppingItem>, draggedIndex: Int, targetPosition: Int) =
        repository.moveItem(items, draggedIndex, targetPosition)
}