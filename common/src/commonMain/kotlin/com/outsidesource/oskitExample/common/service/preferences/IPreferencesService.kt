package com.outsidesource.oskitExample.common.service.preferences

enum class AppColorTheme {
    Dark,
    Light,
}

interface IPreferencesService {
    fun setTheme(theme: AppColorTheme)
    fun getTheme(): AppColorTheme
}