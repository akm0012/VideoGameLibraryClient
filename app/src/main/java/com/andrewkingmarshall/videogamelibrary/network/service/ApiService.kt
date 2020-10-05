package com.andrewkingmarshall.videogamelibrary.network.service

import com.andrewkingmarshall.videogamelibrary.network.dtos.MediaDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameDto
import com.andrewkingmarshall.videogamelibrary.network.dtos.VideoGameIdListDto
import io.reactivex.Observable
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

class ApiService @Inject constructor(retrofit: Retrofit) {

    private var apiServiceInterface: ApiServiceInterface =
        retrofit.create(ApiServiceInterface::class.java)

    fun getVideoGameIds(): Observable<VideoGameIdListDto> {
        Timber.d("Getting Video Game Ids")
        return apiServiceInterface.getVideoGameIds()
    }

    fun getVideoGame(gameId: Int): Observable<VideoGameDto> {
        Timber.d("Getting Video Game $gameId")
        return apiServiceInterface.getVideoGame(gameId)
    }

    fun getVideoGameMedia(gameId: Int): Observable<MediaDto> {
        Timber.d("Getting Video Game Media $gameId")
        return apiServiceInterface.getVideoGameMedia(gameId)
    }

}