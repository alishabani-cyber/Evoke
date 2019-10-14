package com.example.evoke

import android.app.Application
import timber.log.Timber

class BaseApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}