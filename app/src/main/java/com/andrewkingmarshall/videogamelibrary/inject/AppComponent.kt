package com.andrewkingmarshall.videogamelibrary.inject

import com.andrewkingmarshall.videogamelibrary.repository.VideoGameRepository
import com.andrewkingmarshall.videogamelibrary.ui.MainActivity
import com.andrewkingmarshall.videogamelibrary.viewmodel.MainActivityViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(viewModel: MainActivityViewModel)

    fun inject(repository: VideoGameRepository)
}
