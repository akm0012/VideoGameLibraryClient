package com.andrewkingmarshall.videogamelibrary.inject

import android.content.Context
import androidx.room.Room
import com.andrewkingmarshall.videogamelibrary.database.AppDatabase
import com.andrewkingmarshall.videogamelibrary.database.VideoGameDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "VideoGame-Database"
        ).build()
    }

    @Provides
    fun provideVideoGameDao(db: AppDatabase): VideoGameDao {
        return db.videoGameDao()
    }
}
