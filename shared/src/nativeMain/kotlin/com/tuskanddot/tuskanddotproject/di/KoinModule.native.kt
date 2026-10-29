package com.tuskanddot.tuskanddotproject.di

import com.tuskanddot.tuskanddotproject.database.AppDatabase
import com.tuskanddot.tuskanddotproject.database.getDatabaseBuilder
import com.tuskanddot.tuskanddotproject.database.getRoomDatabase
import com.tuskanddot.tuskanddotproject.prefs.UserPreferences
import org.koin.dsl.module


actual val targetModule = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder()
        getRoomDatabase(builder)
    }

    single<UserPreferences> { UserPreferences() }


}