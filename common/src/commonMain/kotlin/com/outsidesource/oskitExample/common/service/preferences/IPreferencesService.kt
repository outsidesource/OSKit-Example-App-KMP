package com.outsidesource.oskitExample.common.service.preferences

enum class AppTheme {
    Dark,
    Light,
}

interface IPreferencesService {
    fun setTheme(theme: AppTheme)
    fun getTheme(): AppTheme
}