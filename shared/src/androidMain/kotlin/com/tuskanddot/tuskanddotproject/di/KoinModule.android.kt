package com.tuskanddot.tuskanddotproject.di

import com.tuskanddot.tuskanddotproject.database.AppDatabase
import com.tuskanddot.tuskanddotproject.database.getDatabaseBuilder
import com.tuskanddot.tuskanddotproject.database.getRoomDatabase
import com.tuskanddot.tuskanddotproject.prefs.UserPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val targetModule: Module = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder(context = get())
        getRoomDatabase(builder)
    }

    single<UserPreferences> { UserPreferences(androidContext()) }
}