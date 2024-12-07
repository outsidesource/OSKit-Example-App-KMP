package com.outsidesource.oskitExample.common.service.preferences

import com.outsidesource.oskitkmp.filesystem.KmpFileRef

enum class AppColorTheme {
    Dark,
    Light,
}

interface IPreferencesService {
    suspend fun setTheme(theme: AppColorTheme)
    suspend fun getTheme(): AppColorTheme
    suspend fun setSelectedFile(ref: KmpFileRef?)
    suspend fun getSelectedFile(): KmpFileRef?
    suspend fun setSelectedFolder(ref: KmpFileRef?)
    suspend fun getSelectedFolder(): KmpFileRef?
}