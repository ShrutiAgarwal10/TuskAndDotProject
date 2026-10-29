package com.tuskanddot.tuskanddotproject.di

import com.tuskanddot.tuskanddotproject.database.AppDatabase
import com.tuskanddot.tuskanddotproject.database.getDatabaseBuilder
import com.tuskanddot.tuskanddotproject.database.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val targetModule: Module = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder(context = get())
        getRoomDatabase(builder)
    }
}