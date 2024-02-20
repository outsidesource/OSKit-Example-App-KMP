package com.outsidesource.oskitExample.common.service.preferences

import com.outsidesource.oskitkmp.file.KMPFileRef

enum class AppColorTheme {
    Dark,
    Light,
}

interface IPreferencesService {
    suspend fun setTheme(theme: AppColorTheme)
    suspend fun getTheme(): AppColorTheme
    suspend fun setSelectedFile(ref: KMPFileRef?)
    suspend fun getSelectedFile(): KMPFileRef?
    suspend fun setSelectedFolder(ref: KMPFileRef?)
    suspend fun getSelectedFolder(): KMPFileRef?
}