package com.andrewkingmarshall.videogamelibrary.inject

import com.andrewkingmarshall.videogamelibrary.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
}
