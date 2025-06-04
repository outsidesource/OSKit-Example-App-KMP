package ui.kvstoreDemo

import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.outcome.Outcome
import com.outsidesource.oskitkmp.outcome.unwrapOrNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import service.kvstoreDemo.IKVStoreDemoService
import service.kvstoreDemo.TodoItem

data class KVStoreDemoScreenViewState(
    val todoItems: List<TodoItem> = emptyList(),
    val newTodoText: String? = null,
    val editableName: String? = null,
    val selectedTodoItem: TodoItem? = null
)

class KVStoreDemoScreenViewInteractor(
    private val kvStoreDemoService: IKVStoreDemoService,
) : Interactor<KVStoreDemoScreenViewState>(
    initialState = KVStoreDemoScreenViewState()
) {

    fun onViewMounted() {
        interactorScope.launch {
            kvStoreDemoService.observeTodos().collect { todos ->
                val currentSelectedItem = state.selectedTodoItem

                update { state ->
                    state.copy(
                        todoItems = todos.orEmpty(),
                        selectedTodoItem = todos.orEmpty().find { it.id == currentSelectedItem?.id },
                    )
                }
            }
        }
    }

    fun newTodoNameTyped(value: String) {
        update { state -> state.copy(newTodoText = value) }
    }

    fun createTodoItemClicked() {
        val name = state.newTodoText
        if (name.isNullOrBlank()) return

        interactorScope.launch {
            val res = kvStoreDemoService.addTodoItem(name)
            if (res is Outcome.Ok) {
                update { state -> state.copy(newTodoText = null) }
            }
        }
    }

    fun toDoItemCompletionStatusChanged(item: TodoItem, completed: Boolean) {
        interactorScope.launch {
            kvStoreDemoService.changeState(item.id, completed)
        }
    }

    fun deleteCurrentItemClicked() {
        val item = state.selectedTodoItem ?: return
        interactorScope.launch {
            kvStoreDemoService.removeTodoItem(item.id)
        }
    }

    fun selectTodoItem(id: String) {
        val item = state.todoItems.find { it.id == id } ?: return
        update { state -> state.copy(selectedTodoItem = item, editableName = item.name) }
    }

    fun currentToDoItemNameEdited(value: String) {
        if (value.isNotBlank()) {
            update { state -> state.copy(editableName = value) }
        }
    }

    fun saveCurrentTodoItemNewName() {
        val item = state.selectedTodoItem ?: return
        val value = state.editableName ?: return

        if (value.isNotBlank()) {
            interactorScope.launch {
                kvStoreDemoService.rename(item.id, value)
            }
        }
    }


}
