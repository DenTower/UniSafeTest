package org.example.project.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.model.ShoppingList
import org.example.project.domain.usecase.AddToShopListUseCase
import org.example.project.domain.usecase.CreateShopListUseCase
import org.example.project.domain.usecase.CrossItemOffUseCase
import org.example.project.domain.usecase.GetAllShopListsUseCase
import org.example.project.domain.usecase.GetShopListUseCase
import org.example.project.domain.usecase.LoginUseCase
import org.example.project.domain.usecase.MoveItemUseCase
import org.example.project.domain.usecase.RegisterUseCase
import org.example.project.domain.usecase.RemoveItemFromShopListUseCase
import org.example.project.domain.usecase.RemoveShopListUseCase
import org.example.project.domain.usecase.UpdateItemShopListUseCase
import org.example.project.util.currentTime
import org.example.project.util.onError
import org.example.project.util.onSuccess

class ShoppingViewModel(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val getAllShopListsUseCase: GetAllShopListsUseCase,
    private val getShopListUseCase: GetShopListUseCase,
    private val createShopListUseCase: CreateShopListUseCase,
    private val removeShopListUseCase: RemoveShopListUseCase,
    private val addToShopListUseCase: AddToShopListUseCase,
    private val updateItemShopListUseCase: UpdateItemShopListUseCase,
    private val removeItemFromShopListUseCase: RemoveItemFromShopListUseCase,
    private val crossItemOffUseCase: CrossItemOffUseCase,
    private val moveItemUseCase: MoveItemUseCase
): ViewModel() {

    private val _state = MutableStateFlow(ShoppingState())
    val state = _state.asStateFlow()

    fun onKeyChanged(key: String) {
        _state.update { it.copy(key = key) }
    }

    init {
        autoRefresh()
    }

    private fun autoRefresh() {
        viewModelScope.launch {
            while (true) {
                if(state.value.isAuth) {
                    getAllShopLists()
                }
                delay(30 * 1000)
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            registerUseCase()
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            key = result,
                            isAuth = result.isNotBlank(),
                            loading = false
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(error = error.name, loading = false)
                    }
                }
        }
    }

    fun login() {
        val key = _state.value.key
        if(key.isBlank()) {
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            loginUseCase(key)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isAuth = result.success,
                            loading = false
                        )
                    }
                }
                .onError { error ->
                    _state.update { it.copy(error = error.name, loading = false) }
                }
        }
    }

    fun logout() {
        _state.update { ShoppingState() }
    }

    fun getAllShopLists() {
        val key = _state.value.key
        if(key.isBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            getAllShopListsUseCase(key)
                .onSuccess { result ->
                    _state.update {
                        val oldListsMap = it.lists.associateBy { it.id }

                        val mergedLists = result.map { newList ->
                            val oldList = oldListsMap[newList.id]

                            if (oldList != null) {
                                newList.copy(items = oldList.items)
                            } else {
                                newList
                            }
                        }

                        it.copy(
                            lists = mergedLists,
                            listIdToIndex = mergedLists
                                .withIndex()
                                .associate { (index, list) ->
                                    list.id to index
                                },
                            loading = false
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(error = error.name, loading = false)
                    }
                }
        }
    }

    fun getShopList(listId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            getShopListUseCase(listId)
                .onSuccess { result ->
                    _state.update {
                        val index = it.listIdToIndex[listId]

                        if(index == null) {
                            it.copy(loading = false)
                        } else {
                            val newLists = it.lists.toMutableList()
                            val shopList = newLists[index]
                            newLists[index] = ShoppingList(
                                created = shopList.created,
                                name = shopList.name,
                                id = shopList.id,
                                items = result
                            )

                            it.copy(lists = newLists, loading = false)
                        }
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(error = error.name, loading = false)
                    }
                }
        }
    }

    fun createShopList(name: String) {
        val key = _state.value.key

        if(key.isNotBlank()) {
            viewModelScope.launch {
                _state.update { it.copy(loading = true, error = null) }

                createShopListUseCase(key = key, name = name)
                    .onSuccess { listId ->
                        _state.update {
                            it.copy(
                                lists = it.lists.plus(
                                    ShoppingList(
                                        created = currentTime(),
                                        name = name,
                                        id = listId,
                                        items = emptyList()
                                    )
                                ),
                                listIdToIndex = it.listIdToIndex.plus(
                                    listId to it.lists.size
                                ),
                                loading = false
                            )
                        }
                    }
                    .onError { error ->
                        _state.update {
                            it.copy(error = error.name, loading = false)
                        }
                    }
            }
        }
    }

    fun removeShopList(listId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            removeShopListUseCase(listId)
                .onSuccess { result ->
                    _state.update {
                        val index = it.listIdToIndex[listId]
                        it.copy(
                            lists = it.lists.toMutableList().apply {
                                index?.let {
                                    removeAt(index)
                                }
                            },
                            listIdToIndex = it.listIdToIndex.toMutableMap().apply {
                                remove(listId)
                            },
                            loading = false
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(error = error.name, loading = false)
                    }
                }

        }
    }

    fun toggleList(shopListIndex: Int) {
        val listId = state.value.lists[shopListIndex].id

        viewModelScope.launch {
            if(state.value.expandedSetIds.contains(listId)) {
                _state.update {
                    it.copy(expandedSetIds = it.expandedSetIds.toMutableSet().apply {
                        remove(listId)
                    })
                }
            } else {
                getShopList(listId)
                _state.update {
                    it.copy(expandedSetIds = it.expandedSetIds.toMutableSet().apply {
                        add(listId)
                    })
                }
            }
        }
    }

    fun addToShopList(shopListIndex: Int, value: String, n: Int) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            val listId = state.value.lists[shopListIndex].id
            addToShopListUseCase(listId, value, n)
                .onSuccess { result ->
                    getShopList(listId)
                }
                .onError { error ->
                    _state.update {
                        it.copy(error = error.name, loading = false)
                    }
                }

        }
    }


    fun updateItemShopList(shopListIndex: Int, itemIndex: Int, name: String, n: Int) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            val itemId = state.value.lists[shopListIndex].items[itemIndex].id
            updateItemShopListUseCase(itemId, name, n)
                .onSuccess { result ->
                    _state.update {
                        val currentList = it.lists[shopListIndex]
                        val currentItem = currentList.items[itemIndex]
                        val newItems = currentList.items.toMutableList().apply {
                            set(itemIndex, currentItem.copy(name = name, n = n))
                        }
                        val newLists = it.lists.toMutableList().apply {
                            set(shopListIndex, currentList.copy(items = newItems))
                        }

                        it.copy(lists = newLists, loading = false)
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(error = error.name, loading = false)
                    }
                }

        }
    }

    fun removeItemFromList(shopListIndex: Int, itemIndex: Int) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            val itemId = state.value.lists[shopListIndex].items[itemIndex].id
            removeItemFromShopListUseCase(itemId)
                .onSuccess { result ->
                    _state.update {
                        val currentList = it.lists[shopListIndex]
                        val newItems = currentList.items.toMutableList().apply {
                            removeAt(itemIndex)
                        }
                        val newLists = it.lists.toMutableList().apply {
                            set(shopListIndex, currentList.copy(items = newItems))
                        }

                        it.copy(lists = newLists, loading = false)
                    }
                }
                .onError { error ->
                    _state.update { it.copy(error = error.name, loading = false) }
                }
        }
    }


    fun crossItemOff(shopListIndex: Int, itemIndex: Int) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            val itemId = state.value.lists[shopListIndex].items[itemIndex].id
            crossItemOffUseCase(itemId)
                .onSuccess { result ->
                    _state.update {
                        val currentList = it.lists[shopListIndex]
                        val currentItem = currentList.items[itemIndex]
                        val newItems = currentList.items.toMutableList().apply {
                            set(itemIndex, currentItem.copy(isCrossed = true))
                        }
                        val newLists = it.lists.toMutableList().apply {
                            set(shopListIndex, currentList.copy(items = newItems))
                        }

                        it.copy(lists = newLists, loading = false)
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(error = error.name, loading = false)
                    }
                }

        }
    }

    fun moveItem(shopListIndex: Int, draggedIndex: Int, targetPosition: Int) {
        if(targetPosition !in draggedIndex..(draggedIndex + 1)) {
            viewModelScope.launch {
                _state.update { it.copy(loading = true, error = null) }

                moveItemUseCase(state.value.lists[shopListIndex].items, draggedIndex, targetPosition)

                getShopList(state.value.lists[shopListIndex].id)
            }
        }
    }
}