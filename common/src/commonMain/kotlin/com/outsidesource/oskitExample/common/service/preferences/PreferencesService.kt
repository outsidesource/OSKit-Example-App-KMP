package com.outsidesource.oskitExample.common.service.preferences

import com.outsidesource.oskitkmp.outcome.unwrapOrNull
import com.outsidesource.oskitkmp.storage.IKMPStorage

class PreferencesService(
    private val storage: IKMPStorage,
) : IPreferencesService {
    private val node = storage.openNode("preferences").unwrapOrNull()

    override fun setTheme(theme: AppColorTheme) {
        node?.putInt("theme", when (theme) {
            AppColorTheme.Light -> 0
            AppColorTheme.Dark -> 1
        })
    }

    override fun getTheme(): AppColorTheme {
        return when (node?.getInt("theme")) {
            0 -> AppColorTheme.Light
            1 -> AppColorTheme.Dark
            else -> AppColorTheme.Light
        }
    }
}