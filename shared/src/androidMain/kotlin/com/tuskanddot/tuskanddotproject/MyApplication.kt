package com.tuskanddot.tuskanddotproject

import android.app.Application
import com.tuskanddot.tuskanddotproject.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initializeKoin(
            config = { androidContext(this@MyApplication) }
        )
    }
}