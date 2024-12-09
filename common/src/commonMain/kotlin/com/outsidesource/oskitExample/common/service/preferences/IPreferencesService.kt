package com.outsidesource.oskitExample.common.service.preferences

import com.outsidesource.oskitkmp.filesystem.KmpFsRef

enum class AppColorTheme {
    Dark,
    Light,
}

interface IPreferencesService {
    suspend fun setTheme(theme: AppColorTheme)
    suspend fun getTheme(): AppColorTheme
    suspend fun setSelectedFile(ref: KmpFsRef?)
    suspend fun getSelectedFile(): KmpFsRef?
    suspend fun setSelectedFolder(ref: KmpFsRef?)
    suspend fun getSelectedFolder(): KmpFsRef?
}