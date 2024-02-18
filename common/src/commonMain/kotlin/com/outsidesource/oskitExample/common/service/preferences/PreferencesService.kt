package com.outsidesource.oskitExample.common.service.preferences

import com.outsidesource.oskitkmp.outcome.unwrapOrNull
import com.outsidesource.oskitkmp.storage.IKMPStorage

class PreferencesService(
    private val storage: IKMPStorage,
) : IPreferencesService {
    private val node = storage.openNode("preferences").unwrapOrNull()

    override fun setTheme(theme: AppTheme) {
        node?.putInt("theme", when (theme) {
            AppTheme.Light -> 0
            AppTheme.Dark -> 1
        })
    }

    override fun getTheme(): AppTheme {
        return when (node?.getInt("theme")) {
            0 -> AppTheme.Light
            1 -> AppTheme.Dark
            else -> AppTheme.Light
        }
    }
}