package com.andrewkingmarshall.videogamelibrary.inject

import android.content.Context
import androidx.room.Room
import com.andrewkingmarshall.videogamelibrary.BuildConfig
import com.andrewkingmarshall.videogamelibrary.R
import com.andrewkingmarshall.videogamelibrary.database.AppDatabase
import com.andrewkingmarshall.videogamelibrary.database.VideoGameDao
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideHttpClient(): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor()

        // Set Log Level
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        // Set up the HTTP Client
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(@ApplicationContext context: Context, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_endpoint))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(httpClient)
            .build()
    }
}