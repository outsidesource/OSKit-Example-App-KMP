package com.outsidesource.oskitExample.common.service.preferences

import com.outsidesource.oskitkmp.file.KMPFileRef
import com.outsidesource.oskitkmp.outcome.unwrapOrNull
import com.outsidesource.oskitkmp.storage.IKMPStorage

private const val KEY_COLOR_THEME = "color-theme"
private const val KEY_SELECTED_FILE = "selected-file"
private const val KEY_SELECTED_FOLDER = "selected-folder"

class PreferencesService(
    private val storage: IKMPStorage,
) : IPreferencesService {
    private val node = storage.openNode("preferences").unwrapOrNull()

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

    override suspend fun setSelectedFile(ref: KMPFileRef?) {
        if (ref == null) {
            node?.remove(KEY_SELECTED_FILE)
            return
        }
        node?.putBytes(KEY_SELECTED_FILE, ref.toPersistableData())
    }

    override suspend fun getSelectedFile(): KMPFileRef? {
        val storedValue = node?.getBytes(KEY_SELECTED_FILE) ?: return null
        return KMPFileRef.fromPersistableData(storedValue)
    }

    override suspend fun setSelectedFolder(ref: KMPFileRef?) {
        if (ref == null) {
            node?.remove(KEY_SELECTED_FOLDER)
            return
        }
        node?.putBytes(KEY_SELECTED_FOLDER, ref.toPersistableData())
    }

    override suspend fun getSelectedFolder(): KMPFileRef? {
        val storedValue = node?.getBytes(KEY_SELECTED_FOLDER) ?: return null
        return KMPFileRef.fromPersistableData(storedValue)
    }
}