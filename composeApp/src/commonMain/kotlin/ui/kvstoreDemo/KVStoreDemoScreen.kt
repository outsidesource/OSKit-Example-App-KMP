package ui.kvstoreDemo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import service.kvstoreDemo.TodoItem
import ui.common.Screen

@Composable
fun KVStoreDemoScreen(
    interactor: KVStoreDemoScreenViewInteractor = rememberInjectForRoute()
) {
    LaunchedEffect(Unit) {
        interactor.onViewMounted()
    }

    val state = interactor.collectAsState()
    val focusManager = LocalFocusManager.current


    Screen("Todo List Demo") {
        Row(Modifier.fillMaxSize()) {
            // Column 1: Input field and Todo List
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                // Input field for new todo item
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.newTodoText ?: "",
                        singleLine = true,
                        onValueChange = { interactor.newTodoNameTyped(it) },
                        label = { Text("New Todo Item") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (!state.newTodoText.isNullOrBlank()) {
                                    interactor.createTodoItemClicked()
                                }
                                focusManager.clearFocus() // Optionally clear focus
                            }
                        ),

                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    Button(
                        onClick = {
                            if (!state.newTodoText.isNullOrBlank()) {
                                interactor.createTodoItemClicked()
                            }
                        },
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text("Add")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Todo List
                Text("Todo Items")
                LazyColumn(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(state.todoItems, key = { it.id }) { item ->
                        TodoListItem(
                            item = item,
                            isSelected = item.id == state.selectedTodoItem?.id,
                            onItemClick = { interactor.selectTodoItem(item.id) },
                            onItemCheckChanged = { interactor.toDoItemCompletionStatusChanged(item, it) }
                        )
                        Divider()
                    }
                }
            }

            // Column 2: Selected Todo Item Details (conditionally visible)
            if (state.selectedTodoItem != null) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Edit Item")
                    Spacer(Modifier.height(16.dp))

                    // Name (editable)
                    OutlinedTextField(
                        value = state.editableName ?: "",
                        onValueChange = {
                            interactor.currentToDoItemNameEdited(it)
                        },
                        label = { Text("Item Name") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                interactor.saveCurrentTodoItemNewName()
                                focusManager.clearFocus()
                            }
                        ),

                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // Completed Checkbox
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = state.selectedTodoItem.completed,
                            onCheckedChange = { isChecked ->
                                interactor.toDoItemCompletionStatusChanged(state.selectedTodoItem, isChecked)
                            }
                        )
                        Text("Completed")
                    }

                    Spacer(Modifier.height(16.dp))

                    // Delete Button
                    Button(
                        onClick = {
                            interactor.deleteCurrentItemClicked()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete Item")
                        Spacer(Modifier.width(4.dp))
                        Text("Delete Item", color = MaterialTheme.colors.onError)
                    }
                }
            }

        }
    }
}

@Composable
fun TodoListItem(
    item: TodoItem,
    isSelected: Boolean,
    onItemClick: () -> Unit,
    onItemCheckChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        Checkbox(
            checked = item.completed,
            onCheckedChange = { onItemCheckChanged(it) },
            enabled = true,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

