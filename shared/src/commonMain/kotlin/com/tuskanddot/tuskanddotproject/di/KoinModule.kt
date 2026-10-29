package com.tuskanddot.tuskanddotproject.di

import com.tuskanddot.tuskanddotproject.database.AppDatabase
import com.tuskanddot.tuskanddotproject.ui.AffirmationViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null){
    startKoin {
        config?.invoke(this)
        modules(targetModule, sharedModule)
    }
}

expect val targetModule: Module

val sharedModule = module {
    single { get<AppDatabase>().getAffirmationDao() }
    viewModel { AffirmationViewModel(get()) }
}
