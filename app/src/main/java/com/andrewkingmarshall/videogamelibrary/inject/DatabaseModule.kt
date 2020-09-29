package com.andrewkingmarshall.videogamelibrary.inject

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.realm.Realm

@Module
@InstallIn(ActivityComponent::class)
class DatabaseModule {

    @Provides
    fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }

}
