package service.kvstoreDemo

import com.outsidesource.oskitkmp.outcome.Outcome
import com.outsidesource.oskitkmp.outcome.unwrapOrNull
import com.outsidesource.oskitkmp.storage.IKmpKvStore
import com.outsidesource.oskitkmp.storage.IKmpKvStoreNode
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.builtins.ListSerializer

private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

private const val KEY_ITEMS = "items"


class KVStoreDemoService(
    private val storage: IKmpKvStore,
) : IKVStoreDemoService {

    private val node = CompletableDeferred<IKmpKvStoreNode?>()

    init {
        scope.launch {
            node.complete(storage.openNode("kvstoredemo").unwrapOrNull())
        }
    }

    override suspend fun observeTodos(): Flow<List<TodoItem>?> {
        return node.await()?.observeSerializable(KEY_ITEMS, ListSerializer(TodoItem.serializer())) ?: emptyFlow()
    }

    override suspend fun addTodoItem(title: String): Outcome<TodoItem, Any> {
        val data = readItemsSnapshot()
        val entity = TodoItem(title, title, false)
        val res = writeItemsSnapshot(data.toMutableList().apply { add(entity) })

        return when {
            res != null && res is Outcome.Ok -> Outcome.Ok(entity)
            res is Outcome.Error -> Outcome.Error(res.error)
            else -> Outcome.Error(IllegalStateException("Node is null"))
        }
    }

    override suspend fun removeTodoItem(id: String): Boolean {
        val data = readItemsSnapshot().toMutableList()
        val item = data.find { it.id == id }

        return if (item != null) {
            data.remove(item)
            writeItemsSnapshot(data)
            true
        } else {
            false
        }
    }

    override suspend fun changeState(id: String, completed: Boolean): Boolean {
        val data = readItemsSnapshot().toMutableList()
        val item = data.find { it.id == id }

        return if (item != null) {
            val index = data.indexOf(item)
            data[index] = item.copy(completed = completed)
            writeItemsSnapshot(data)
            true
        } else {
            false
        }
    }

    private suspend fun readItemsSnapshot(): List<TodoItem> {
        return node.await()?.getSerializable(
            KEY_ITEMS,
            ListSerializer(TodoItem.serializer())
        ).orEmpty().toMutableList()
    }

    private suspend fun writeItemsSnapshot(data: List<TodoItem>): Outcome<Unit, Any>? {
        return node.await()?.putSerializable(KEY_ITEMS, data, ListSerializer(TodoItem.serializer()))
    }

}