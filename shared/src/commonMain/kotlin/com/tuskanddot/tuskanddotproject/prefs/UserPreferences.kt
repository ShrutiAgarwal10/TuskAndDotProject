package com.tuskanddot.tuskanddotproject.prefs

import kotlinx.coroutines.flow.Flow


internal const val dataStoreFileName = "userDefaults"

expect class UserPreferences{

    suspend fun <T> savePreference(key: String, value: T)

    fun <T> getPreference(key: String, defaultValue: T): Flow<T>
}