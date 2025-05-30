package service.kvstoreDemo

import com.outsidesource.oskitkmp.filesystem.KmpFsRef
import com.outsidesource.oskitkmp.filesystem.KmpFsType
import com.outsidesource.oskitkmp.outcome.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import ui.kvstoreDemo.KVStoreDemoScreenViewState

interface IKVStoreDemoService {
    suspend fun observeTodos(): Flow<List<TodoItem>?>
    suspend fun addTodoItem(title: String): Outcome<TodoItem, Any>
    suspend fun removeTodoItem(id: String): Boolean
    suspend fun changeState(id: String, completed: Boolean): Boolean
    suspend fun rename(id: String, name: String): Boolean
}

@Serializable
data class TodoItem(
    val id: String,
    val name: String,
    val completed: Boolean
)