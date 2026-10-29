package com.tuskanddot.tuskanddotproject.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = dataStoreFileName)

actual class UserPreferences(
    private val context: Context
) {
    actual suspend fun <T> savePreference(key: String, value: T) {
        val dataStoreKey = getDataStoreKey(key, value) // Pass class type
        context.dataStore.edit { preferences ->
            when (value) {
                is Boolean -> preferences[dataStoreKey as Preferences.Key<Boolean>] = value
                is Int -> preferences[dataStoreKey as Preferences.Key<Int>] = value
                is String -> preferences[dataStoreKey as Preferences.Key<String>] = value
                is Float -> preferences[dataStoreKey as Preferences.Key<Float>] = value
                is Double -> preferences[dataStoreKey as Preferences.Key<Double>] = value
            }
        }
    }

    actual fun <T> getPreference(key: String, defaultValue: T): Flow<T> {
        val dataStoreKey = getDataStoreKey(key, defaultValue) // Pass class type
        return context.dataStore.data.map { preferences ->
            when (defaultValue) {
                is Boolean -> preferences[dataStoreKey as Preferences.Key<Boolean>] ?: defaultValue
                is Int -> preferences[dataStoreKey as Preferences.Key<Int>] ?: defaultValue
                is String -> preferences[dataStoreKey as Preferences.Key<String>] ?: defaultValue
                is Float -> preferences[dataStoreKey as Preferences.Key<Float>] ?: defaultValue
                is Double -> preferences[dataStoreKey as Preferences.Key<Double>] ?: defaultValue
                else -> defaultValue
            } as T
        }
    }

    private fun <T> getDataStoreKey(key: String, clazz: T): Preferences.Key<T> {
        println()
        return when (clazz) {
            is Boolean -> booleanPreferencesKey(key)
            is Int -> intPreferencesKey(key)
            is String -> stringPreferencesKey(key)
            is Float -> floatPreferencesKey(key)
            is Double -> doublePreferencesKey(key)
            else -> throw IllegalArgumentException("Unsupported data type")
        } as Preferences.Key<T>
    }

}
