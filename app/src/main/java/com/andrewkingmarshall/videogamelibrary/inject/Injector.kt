package com.andrewkingmarshall.videogamelibrary.inject

import android.content.Context
import com.andrewkingmarshall.videogamelibrary.VideoGameLibraryApplication
import java.lang.IllegalStateException

/**
 * This Injector is an "Object" so it should behave like a Singleton.
 *
 * In the past, I have always needed to pass Context to Dagger in order to Inject things. So I am trying a new pattern
 * where you call "init" in the Application. This will provide Context needed to set this up.
 */
object Injector {

    private var appComponent :AppComponent? = null

    /**
     * This must be set before this will work.
     */
    fun init(context: Context) {
        appComponent = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(context.applicationContext as VideoGameLibraryApplication))
            .build()
    }

    fun obtain() : AppComponent {
        if (appComponent == null) {
            throw IllegalStateException("Dagger was not set up correctly. Must call init(context).")
        } else {
            return appComponent as AppComponent
        }
    }
}
