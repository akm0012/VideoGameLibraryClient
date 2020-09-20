package com.andrewkingmarshall.videogamelibrary

import android.app.Application
import com.andrewkingmarshall.videogamelibrary.inject.Injector

class VideoGameLibraryApplication  : Application() {

    override fun onCreate() {
        super.onCreate()

        setUpDagger()
    }

    private fun setUpDagger() {
        Injector.init(this)
    }
}