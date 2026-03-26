package org.example.project.presentation.viewmodel

import org.example.project.domain.model.ShoppingList

data class ShoppingState (
    val lists: List<ShoppingList> = emptyList(),
    val listIdToIndex: Map<Long, Int> = emptyMap(),
    val key: String = "",
    val expandedSetIds: Set<Long> = emptySet(),
    val isAuth: Boolean = false,
    val error: String? = null,
    val loading: Boolean = false
)
