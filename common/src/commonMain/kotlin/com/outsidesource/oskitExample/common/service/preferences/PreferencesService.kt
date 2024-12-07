package com.outsidesource.oskitExample.common.service.preferences

import com.outsidesource.oskitkmp.filesystem.KmpFileRef
import com.outsidesource.oskitkmp.outcome.unwrapOrNull
import com.outsidesource.oskitkmp.storage.IKmpKvStore
import com.outsidesource.oskitkmp.storage.IKmpKvStoreNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private const val KEY_COLOR_THEME = "color-theme"
private const val KEY_SELECTED_FILE = "selected-file"
private const val KEY_SELECTED_FOLDER = "selected-folder"

private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

class PreferencesService(
    private val storage: IKmpKvStore,
) : IPreferencesService {

    private var node: IKmpKvStoreNode? = null

    init {
        scope.launch {
            node = storage.openNode("preferences").unwrapOrNull()
        }
    }

    override suspend fun setTheme(theme: AppColorTheme) {
        node?.putInt(KEY_COLOR_THEME, when (theme) {
            AppColorTheme.Light -> 0
            AppColorTheme.Dark -> 1
        })
    }

    override suspend fun getTheme(): AppColorTheme {
        return when (node?.getInt(KEY_COLOR_THEME)) {
            0 -> AppColorTheme.Light
            1 -> AppColorTheme.Dark
            else -> AppColorTheme.Light
        }
    }

    override suspend fun setSelectedFile(ref: KmpFileRef?) {
        if (ref == null) {
            node?.remove(KEY_SELECTED_FILE)
            return
        }
        node?.putBytes(KEY_SELECTED_FILE, ref.toPersistableData())
    }

    override suspend fun getSelectedFile(): KmpFileRef? {
        val storedValue = node?.getBytes(KEY_SELECTED_FILE) ?: return null
        return KmpFileRef.fromPersistableData(storedValue)
    }

    override suspend fun setSelectedFolder(ref: KmpFileRef?) {
        if (ref == null) {
            node?.remove(KEY_SELECTED_FOLDER)
            return
        }
        node?.putBytes(KEY_SELECTED_FOLDER, ref.toPersistableData())
    }

    override suspend fun getSelectedFolder(): KmpFileRef? {
        val storedValue = node?.getBytes(KEY_SELECTED_FOLDER) ?: return null
        return KmpFileRef.fromPersistableData(storedValue)
    }
}