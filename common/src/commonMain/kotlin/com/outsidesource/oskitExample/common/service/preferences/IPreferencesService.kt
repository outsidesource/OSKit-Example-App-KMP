package com.outsidesource.oskitExample.common.service.preferences

import com.outsidesource.oskitkmp.filesystem.KmpFsRef
import com.outsidesource.oskitkmp.filesystem.KmpFsType

enum class AppColorTheme {
    Dark,
    Light,
}

interface IPreferencesService {
    suspend fun setTheme(theme: AppColorTheme)
    suspend fun getTheme(): AppColorTheme
    suspend fun setPersistedRef(ref: KmpFsRef?, type: KmpFsType)
    suspend fun getPersistedRef(type: KmpFsType): KmpFsRef?
}