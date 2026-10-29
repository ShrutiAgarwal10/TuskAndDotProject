package com.tuskanddot.tuskanddotproject.prefs

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import platform.Foundation.NSUserDefaults

actual class UserPreferences {
    private val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults

    actual suspend fun <T> savePreference(key: String, value: T) {
        when (value) {
            is Boolean -> userDefaults.setBool(value, key)
            is Int -> userDefaults.setInteger(value.toLong(), key)
            is String -> userDefaults.setObject(value, key)
            is Float -> userDefaults.setFloat(value, key)
            is Double -> userDefaults.setDouble(value, key)
            else -> throw IllegalArgumentException("Unsupported data type")
        }
    }

    actual fun <T> getPreference(key: String, defaultValue: T): Flow<T> = flow {
        val value: T = when (defaultValue) {
            is Boolean -> userDefaults.boolForKey(key) as T
            is Int -> userDefaults.integerForKey(key).toInt() as T
            is String -> userDefaults.stringForKey(key) as? T ?: defaultValue
            is Float -> userDefaults.floatForKey(key) as T
            is Double -> userDefaults.doubleForKey(key) as T
            else -> defaultValue
        }
        emit(value)
    }

}