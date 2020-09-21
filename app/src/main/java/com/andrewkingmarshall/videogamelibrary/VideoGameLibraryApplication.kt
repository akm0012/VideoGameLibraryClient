package com.andrewkingmarshall.videogamelibrary

import android.app.Application
import com.andrewkingmarshall.videogamelibrary.inject.Injector
import net.danlew.android.joda.JodaTimeAndroid

class VideoGameLibraryApplication  : Application() {

    override fun onCreate() {
        super.onCreate()

        setUpDagger()
    }

    private fun setUpDagger() {
        Injector.init(this)
    }

    private fun setUpJodaTime() {
        JodaTimeAndroid.init(this)
    }
}