package org.example.project.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.example.project.domain.model.ShoppingItem
import org.example.project.domain.model.ShoppingList
import org.example.project.presentation.viewmodel.ShoppingState
import org.example.project.presentation.viewmodel.ShoppingViewModel
import org.jetbrains.compose.resources.painterResource
import unisafetest.composeapp.generated.resources.Res
import unisafetest.composeapp.generated.resources.add
import unisafetest.composeapp.generated.resources.arrow_drop_down
import unisafetest.composeapp.generated.resources.arrow_drop_up
import unisafetest.composeapp.generated.resources.check
import unisafetest.composeapp.generated.resources.delete
import unisafetest.composeapp.generated.resources.edit

@Composable
fun MainScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: ShoppingViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(true) {
        viewModel.getAllShopLists()
    }

    MainScreenContent(
        state = state,
        onLogout = {
            viewModel.logout()
            onNavigateToLogin()
        },
        onCreateList = viewModel::createShopList,
        onRemoveList = viewModel::removeShopList,
        onToggleList = viewModel::toggleList,
        onAddItem = viewModel::addToShopList,
        onCrossItem = viewModel::crossItemOff,
        onDeleteItem = viewModel::removeItemFromList,
        onMoveItem = viewModel::moveItem,
        onEditItem = viewModel::updateItemShopList

    )
}

@Composable
fun MainScreenContent(
    state: ShoppingState,
    onLogout: () -> Unit,
    onCreateList: (name: String) -> Unit,
    onRemoveList: (listId: Long) -> Unit,
    onToggleList: (listId: Long) -> Unit,
    onAddItem: (listId: Long, name: String, count: Int) -> Unit,
    onCrossItem: (listId: Long, itemId: Long) -> Unit,
    onDeleteItem: (listId: Long, itemId: Long) -> Unit,
    onMoveItem: (listId: Long, draggedIndex: Int, targetPosititon: Int) -> Unit,
    onEditItem: (listId: Long, itemId: Long, name: String, count: Int) -> Unit
) {

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            state.error?.let {
                Text(it, color = Color.Red)
            }

            if(state.loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }


            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onLogout
                ) {
                    Text("Выйти")
                }

                Spacer(Modifier.width(8.dp))

                var showKey by remember { mutableStateOf(false) }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { showKey = !showKey }
                ) {
                    Text(if(showKey) "Ключ: ${state.key}" else "Пригласить")
                }

            }

            Spacer(Modifier.height(24.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val textFieldState = remember { TextFieldState() }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                        .padding(1.dp)
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(12.dp)
                ) {
                    if(textFieldState.text.isBlank()) {
                        Text(
                            text = "Введите название нового списка",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    BasicTextField(
                        state = textFieldState,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                IconButton(
                    onClick = {
                        onCreateList(textFieldState.text.toString())
                        textFieldState.edit {
                            replace(0, length, "")
                        }
                    }) {
                    Icon(
                        painterResource(Res.drawable.add),
                        "Добавить список покупок"
                    )
                }
            }


            state.lists.forEachIndexed { shopListIndex, list ->
                Column {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleList(list.id) },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = list.name,
                            fontWeight = Bold,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(text = list.created)

                        Row {
                            IconButton(onClick = {
                                onRemoveList(list.id)
                            }) {
                                Icon(
                                    painterResource(Res.drawable.delete),
                                    contentDescription = "Удалить список",
                                    tint = Color.Black
                                )
                            }

                            IconButton(onClick = {
                                onToggleList(list.id)
                            }) {
                                if(state.expandedSetIds.contains(list.id)) {
                                    Icon(
                                        painterResource(Res.drawable.arrow_drop_up),
                                        contentDescription = "Свернуть список",
                                        tint = Color.Black
                                    )
                                } else {
                                    Icon(
                                        painterResource(Res.drawable.arrow_drop_down),
                                        contentDescription = "Развернуть список",
                                        tint = Color.Black
                                    )
                                }
                            }
                        }
                    }

                    if(state.expandedSetIds.contains(list.id)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            val nameTextFieldState = rememberTextFieldState()
                            val countTextFieldState = rememberTextFieldState("1")

                            CounterControl(countTextFieldState)

                            Box {
                                BasicTextField(
                                    state = countTextFieldState,
                                    modifier = Modifier.width(48.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }

                            Box(Modifier.weight(1f)) {
                                if(nameTextFieldState.text.isBlank()) {
                                    Text(
                                        text = "Добавить покупку",
                                        color = MaterialTheme.colorScheme.onBackground.copy(
                                            alpha = 0.5f
                                        ),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                BasicTextField(
                                    state = nameTextFieldState,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            IconButton(onClick = {
                                if(nameTextFieldState.text.isNotBlank()) {
                                    onAddItem(
                                        list.id,
                                        nameTextFieldState.text.toString(),
                                        countTextFieldState.text.toString().toInt()
                                    )
                                    nameTextFieldState.edit {
                                        replace(0, length, "")
                                    }
                                }
                            }) {
                                Icon(
                                    painterResource(Res.drawable.add),
                                    "Добавить покупку"
                                )
                            }
                        }

                        val draggedIndex = remember { mutableStateOf<Int?>(null) }
                        val targetPosition = remember { mutableStateOf<Int?>(null) }
                        val offsetY = remember { mutableStateOf(0f) }

                        LazyColumn {
                            itemsIndexed(list.items) { itemIndex, item ->
                                val isDragged = itemIndex == draggedIndex.value
                                val isTarget = itemIndex == targetPosition.value

                                if(isTarget) {
                                    HorizontalDivider(thickness = 2.dp, color = Color.Blue)
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .offset {
                                            IntOffset(
                                                0,
                                                if(isDragged) offsetY.value.toInt() else 0
                                            )
                                        }
                                        .pointerInput(list.items.size) {
                                            detectDragGestures(
                                                onDragStart = {
                                                    draggedIndex.value = itemIndex
                                                    targetPosition.value = itemIndex
                                                },
                                                onDrag = { change, dragAmount ->
                                                    change.consume()
                                                    offsetY.value += dragAmount.y

                                                    val shift =
                                                        (offsetY.value / 150).toInt()
                                                    val newPosition =
                                                        (draggedIndex.value!! + shift)
                                                            .coerceIn(
                                                                0,
                                                                list.items.size
                                                            )
                                                    targetPosition.value = newPosition
                                                },
                                                onDragEnd = {
                                                    if(draggedIndex.value != null && targetPosition.value != null)
                                                        onMoveItem(
                                                            list.id,
                                                            draggedIndex.value!!,
                                                            targetPosition.value!!
                                                        )
                                                    draggedIndex.value = null
                                                    targetPosition.value = null
                                                    offsetY.value = 0f
                                                }
                                            )
                                        }
                                ) {
                                    ShoppingItemRow(
                                        item = item,
                                        onCross = {
                                            onCrossItem(
                                                list.id,
                                                item.id
                                            )
                                        },
                                        onDelete = {
                                            onDeleteItem(
                                                list.id,
                                                item.id
                                            )
                                        },
                                        onEdit = { name, count ->
                                            onEditItem(
                                                list.id, item.id,
                                                name, count
                                            )
                                        },
                                        modifier = if(isDragged) Modifier
                                            .background(Color.LightGray.copy(alpha = 0.8f)) else Modifier
                                    )
                                }

                                if(targetPosition.value == list.items.size && itemIndex == list.items.lastIndex) {
                                    HorizontalDivider(thickness = 2.dp, color = Color.Blue)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ShoppingItemRow(
    item: ShoppingItem,
    onCross: () -> Unit,
    onDelete: () -> Unit,
    onEdit: (name: String, count: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var editMode by remember { mutableStateOf(false) }
    val nameTextFieldState = rememberTextFieldState(item.name)
    val countTextFieldState = rememberTextFieldState(item.n.toString())

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(!editMode) {
                Checkbox(
                    checked = item.isCrossed,
                    onCheckedChange = { onCross() }
                )
            }

            if(editMode) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    CounterControl(state = countTextFieldState)

                    Box {
                        BasicTextField(
                            state = countTextFieldState,
                            modifier = Modifier.width(48.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Box(Modifier.weight(1f)) {
                        BasicTextField(
                            state = nameTextFieldState,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground.copy(
                                    alpha = 0.5f
                                )
                            )
                        )
                    }
                }
            } else {
                Text(
                    text = "${item.n}  ${item.name}",
                    textDecoration = if(item.isCrossed)
                        TextDecoration.LineThrough
                    else null
                )
            }
        }

        Row {
            IconButton(onClick = onDelete) {
                Icon(painterResource(Res.drawable.delete), "Удалить")
            }

            IconButton(onClick = {
                if(editMode) onEdit(
                    nameTextFieldState.text.toString(),
                    countTextFieldState.text.toString().toInt()
                )
                editMode = !editMode
            }) {
                if(editMode) {
                    Icon(painterResource(Res.drawable.check), "Подтвердить")
                } else {
                    Icon(painterResource(Res.drawable.edit), "Изменить")
                }
            }

        }
    }
}

@Composable
fun CounterControl(
    state: TextFieldState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        IconButton(
            modifier = Modifier.size(24.dp),
            onClick = {
                val current = state.text.toString().toIntOrNull() ?: 0
                state.edit {
                    replace(0, length, (current + 1).toString())
                }
            }
        ) {
            Icon(painterResource(Res.drawable.arrow_drop_up), "Увеличить")
        }

        IconButton(
            modifier = Modifier.size(24.dp),
            onClick = {
                val current = state.text.toString().toIntOrNull() ?: 0
                if (current > 0) {
                    state.edit {
                        replace(0, length, (current - 1).toString())
                    }
                }
            }
        ) {
            Icon(painterResource(Res.drawable.arrow_drop_down), "Уменьшить")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {

    val fakeState = ShoppingState(
        lists = listOf(
            ShoppingList(
                id = 1,
                name = "Продукты",
                created = "23.04.26",
                items = listOf(
                    ShoppingItem("Молоко", false, 1, 2),
                    ShoppingItem("Хлеб", true, 2, 3)
                ),
                itemIdToIndex = emptyMap()
            ),
            ShoppingList(
                id = 2,
                name = "Техника",
                created = "23.05.26",
                items = listOf(
                    ShoppingItem("Мышка", false, 3, 1)
                ),
                itemIdToIndex = emptyMap()
            )
        ),
        expandedSetIds = setOf(1),
        loading = false,
        error = null
    )

    MainScreenContent(
        state = fakeState,
        onLogout = {},
        onCreateList = {},
        onRemoveList = {},
        onToggleList = {},
        onAddItem = { _, _, _ -> },
        onCrossItem = { _, _ -> },
        onDeleteItem = { _, _ -> },
        onMoveItem = { _, _, _ -> },
        onEditItem = { _, _, _, _ -> }
    )
}