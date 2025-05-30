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


/**
 * A simple KVStore demo service that demonstrates how to use KmpKvStore to store and retrieve data in a reactive
 * way. Note, the implementation here is simplified for the demo purposes.
 *
 * The KVStoreDemoService is a simple service that stores and retrieves TodoItems in a reactive way. It uses a single
 * KmpKvStore node to store the serialized list of [TodoItem]. The [KmpKvStore] node is opened asynchronously when the service is
 * initialized. The [KmpKvStore] node is closed when the service is destroyed.
 *
 * The [KVStoreDemoService] provides a flow of a list of [TodoItem] that can be observed using the observeTodos() function.
 *
 * The [KVStoreDemoService] provides functions to add, remove, update, and rename TodoItems using the addTodoItem(),
 * removeTodoItem(), changeState(), and rename() functions, respectively.
 *
 * This service is being consumed in the [ui.kvstoreDemo.KVStoreDemoScreenViewInteractor].
 */
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

    override suspend fun rename(id: String, name: String): Boolean {
        val data = readItemsSnapshot().toMutableList()
        val item = data.find { it.id == id }

        return if (item != null) {
            val index = data.indexOf(item)
            data[index] = item.copy(name = name)
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