package service.preferences

import com.outsidesource.oskitkmp.filesystem.KmpFsRef
import com.outsidesource.oskitkmp.filesystem.KmpFsType
import com.outsidesource.oskitkmp.outcome.unwrapOrNull
import com.outsidesource.oskitkmp.storage.IKmpKvStore
import com.outsidesource.oskitkmp.storage.IKmpKvStoreNode
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private const val KEY_COLOR_THEME = "color-theme"
private const val KEY_PERSISTED_INTERNAL_KMPFS_REF = "persisted-internal-ref"
private const val KEY_PERSISTED_EXTERNAL_KMPFS_REF = "persisted-external-ref"

private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

class PreferencesService(
    private val storage: IKmpKvStore,
) : IPreferencesService {

    private val node = CompletableDeferred<IKmpKvStoreNode?>()

    init {
        scope.launch {
            node.complete(storage.openNode("preferences").unwrapOrNull())
        }
    }

    override suspend fun setTheme(theme: AppColorTheme) {
        node.await()?.putInt(KEY_COLOR_THEME, when (theme) {
            AppColorTheme.Light -> 0
            AppColorTheme.Dark -> 1
        })
    }

    override suspend fun getTheme(): AppColorTheme {

        return when (node.await()?.getInt(KEY_COLOR_THEME)) {
            0 -> AppColorTheme.Light
            1 -> AppColorTheme.Dark
            else -> AppColorTheme.Light
        }
    }

    override suspend fun setPersistedRef(ref: KmpFsRef?, type: KmpFsType) {
        val key = when (type) {
            KmpFsType.Internal -> KEY_PERSISTED_INTERNAL_KMPFS_REF
            KmpFsType.External -> KEY_PERSISTED_EXTERNAL_KMPFS_REF
        }

        if (ref == null) {
            node.await()?.remove(key)
            return
        }
        node.await()?.putBytes(key, ref.toPersistableBytes())
    }

    override suspend fun getPersistedRef(type: KmpFsType): KmpFsRef? {
        val key = when (type) {
            KmpFsType.Internal -> KEY_PERSISTED_INTERNAL_KMPFS_REF
            KmpFsType.External -> KEY_PERSISTED_EXTERNAL_KMPFS_REF
        }

        val storedValue = node.await()?.getBytes(key) ?: return null
        return KmpFsRef.fromPersistableBytes(storedValue)
    }
}