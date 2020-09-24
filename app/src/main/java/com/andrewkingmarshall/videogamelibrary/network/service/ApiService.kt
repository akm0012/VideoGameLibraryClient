package com.andrewkingmarshall.videogamelibrary.network.service

import android.content.Context
import com.andrewkingmarshall.videogamelibrary.BuildConfig
import com.andrewkingmarshall.videogamelibrary.R
import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameIdListDto
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiService @Inject constructor(@ApplicationContext private val context: Context) {

    private var apiServiceInterface: ApiServiceInterface

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        // Set Log Level
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        // Set up the HTTP Client
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_endpoint))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(okHttpClient)
            .build()

        apiServiceInterface = retrofit.create(ApiServiceInterface::class.java)
    }

    fun getVideoGameIds(): Observable<VideoGameIdListDto> {
        return apiServiceInterface.getVideoGameIds()
    }

    fun getVideoGame(gameId: Int): Observable<VideoGameDto> {
        return apiServiceInterface.getVideoGame(gameId)
    }

    fun getVideoGameMedia(gameId: Int): Observable<MediaDto> {
        return apiServiceInterface.getVideoGameMedia(gameId)
    }

}