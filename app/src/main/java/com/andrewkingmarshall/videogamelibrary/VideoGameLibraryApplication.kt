package com.andrewkingmarshall.videogamelibrary

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import net.danlew.android.joda.JodaTimeAndroid

@HiltAndroidApp
class VideoGameLibraryApplication  : Application() {

    override fun onCreate() {
        super.onCreate()

        setUpJodaTime()
    }

    private fun setUpJodaTime() {
        JodaTimeAndroid.init(this)
    }
}