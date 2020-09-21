package com.andrewkingmarshall.videogamelibrary.inject

import android.content.Context
import com.andrewkingmarshall.videogamelibrary.VideoGameLibraryApplication
import com.andrewkingmarshall.videogamelibrary.network.service.ApiService
import com.andrewkingmarshall.videogamelibrary.repository.VideoGameRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(
    val application: VideoGameLibraryApplication
) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiService()
    }

    @Provides
    @Singleton
    fun provideVideoGameRepository(): VideoGameRepository {
        return VideoGameRepository()
    }

}
