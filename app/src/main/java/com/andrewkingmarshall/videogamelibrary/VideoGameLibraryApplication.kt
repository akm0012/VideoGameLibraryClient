package com.andrewkingmarshall.videogamelibrary

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber

@HiltAndroidApp
class VideoGameLibraryApplication  : Application() {

    override fun onCreate() {
        super.onCreate()

        setUpJodaTime()

        setUpLogging()
    }

    private fun setUpJodaTime() {
        JodaTimeAndroid.init(this)
    }

    private fun setUpLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}