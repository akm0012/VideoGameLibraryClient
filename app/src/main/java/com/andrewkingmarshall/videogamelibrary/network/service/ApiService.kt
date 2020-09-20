package com.andrewkingmarshall.videogamelibrary.network.service

import com.andrewkingmarshall.videogamelibrary.BuildConfig
import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameIdListDto
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {

    companion object {
        val BASE_URL_ENDPOINT = "https://72f75bf21830.ngrok.io"
    }

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
            .baseUrl(BASE_URL_ENDPOINT)
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