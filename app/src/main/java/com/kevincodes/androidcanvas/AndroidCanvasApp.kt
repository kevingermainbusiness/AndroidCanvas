package com.kevincodes.androidcanvas

import android.app.Application
import timber.log.Timber

class AndroidCanvasApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}